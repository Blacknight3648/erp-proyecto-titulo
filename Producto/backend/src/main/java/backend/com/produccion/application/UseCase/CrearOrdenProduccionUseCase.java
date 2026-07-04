package backend.com.produccion.application.UseCase;

import backend.com.comercial.domain.enums.TipoItem;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.ItemNV;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.produccion.domain.enums.FaseProduccion;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.CosteoVersion;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.model.OrdenProduccionItem;
import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrearOrdenProduccionUseCase {

    private final OrdenProduccionRepository repository;
    private final EvaluacionNegocioRepository evnRepository;
    private final CosteoRepository costeoRepository;
    private final OrdenTrabajoRepository otRepository;
    private final NotaVentaRepository nvRepository;
    private final CrearVersionCosteoUseCase crearVersionCosteoUseCase;
    private final NumeroDocumentoService numeroDocumentoService;

    /**
     * Cada ítem tipo OP que aún no tiene una OP asignada genera su propia Orden de
     * Producción individual, con su propio costeo. Los ítems que ya tenían una OP (de un
     * guardado anterior) quedan intactos — no se tocan ni se les crea una OP nueva.
     */
    @Transactional
    public List<OrdenProduccion> execute(NotaVenta notaVenta) {
        if (notaVenta == null)
            throw new ValidationException("La Nota de Venta no puede ser nula");

        List<ItemNV> itemsSinOP = notaVenta.getItems() == null ? List.of()
                : notaVenta.getItems().stream()
                        .filter(i -> TipoItem.OP == i.getTipoItem() && i.getOpId() == null)
                        .toList();

        List<OrdenProduccion> creadas = new ArrayList<>();
        for (ItemNV item : itemsSinOP) {
            OrdenProduccion op = crearOPParaItem(notaVenta, item);
            nvRepository.vincularOpAItem(item.getIdItemNV(), op.getIdOP());
            creadas.add(op);
        }
        return creadas;
    }

    private OrdenProduccion crearOPParaItem(NotaVenta notaVenta, ItemNV itemNV) {
        // Si el ítem ya tiene un número de OP reservado (del borrador), usarlo.
        // Si no, generar uno nuevo del contador atómico.
        DocumentNumber numeroOP = itemNV.getNumeroOPReservado() != null
                ? new DocumentNumber(itemNV.getNumeroOPReservado())
                : numeroDocumentoService.siguienteFormateado("OP");

        Long costeoVersionId = null;

        // Intento 1: costeo elegido manualmente por el usuario (prioridad máxima).
        // Si el usuario seleccionó un costeo en la UI, ese es el que se usa sin importar
        // lo que traiga la EVN plantilla.
        Long costeoIdManual = itemNV.getCosteoIdManual();
        if (costeoIdManual != null) {
            Costeo costeoManual = costeoRepository.findById(costeoIdManual)
                    .orElseThrow(() -> new EntityNotFoundException("Costeo no encontrado: " + costeoIdManual));

            if (costeoManual.getEstado() != backend.com.produccion.domain.enums.EstadoCosteo.APROBADO) {
                throw new ValidationException(
                        "El Costeo " + costeoIdManual + " debe estar APROBADO para poder vincularse a la OP.");
            }
            if (repository.findCosteoIdsEnUso().contains(costeoIdManual)) {
                throw new ValidationException(
                        "El Costeo " + costeoIdManual + " ya está vinculado a otra Orden de Producción.");
            }

            CosteoVersion versionManual = crearVersionCosteoUseCase.ejecutar(
                    costeoManual.getIdCosteo(),
                    "Costeo existente vinculado manualmente desde la NV",
                    "SYSTEM");
            costeoVersionId = versionManual.getIdCosteoVersion();
        }

        // Intento 2: costeo heredado de la EVN plantilla (solo si no hubo manual).
        if (costeoVersionId == null && notaVenta.getEvaluacionNegocioId() != null) {
            EvaluacionNegocio evn = evnRepository.findById(notaVenta.getEvaluacionNegocioId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Evaluación de Negocio no encontrada: " + notaVenta.getEvaluacionNegocioId()));

            Long costeoIdVinculado = evn.getItems().stream()
                    .filter(i -> TipoItem.OP == i.getTipoItem() && i.getCosteoId() != null)
                    .map(backend.com.comercial.domain.model.ItemEVN::getCosteoId)
                    .findFirst()
                    .orElse(null);

            if (costeoIdVinculado != null) {
                Costeo costeo = costeoRepository.findById(costeoIdVinculado)
                        .orElseThrow(() -> new EntityNotFoundException("Costeo no encontrado: " + costeoIdVinculado));

                CosteoVersion versionInicial = crearVersionCosteoUseCase.ejecutar(
                        costeo.getIdCosteo(),
                        "Versión inicial al crear OP",
                        "SYSTEM");
                costeoVersionId = versionInicial.getIdCosteoVersion();
            }
        }

        // Intento 3 (fallback): crear un costeo vacío nuevo.
        if (costeoVersionId == null) {
            DocumentNumber numeroCosteo = numeroDocumentoService.siguienteFormateado("COST");
            Costeo vacio = Costeo.crearVacio(numeroCosteo, notaVenta.getIdNV());
            Costeo costeoBase = costeoRepository.save(vacio);

            CosteoVersion version = crearVersionCosteoUseCase.ejecutar(
                    costeoBase.getIdCosteo(),
                    "Costeo inicial auto-creado — ítem sin costeo pre-vinculado en EVN",
                    "SYSTEM");
            costeoVersionId = version.getIdCosteoVersion();
        }

        OrdenProduccion op = OrdenProduccion.crearNueva(
                numeroOP,
                notaVenta.getIdNV(),
                notaVenta.getFechaEntregaEstimada());

        // costeoVersionId siempre es no-nulo en este punto (invariante garantizado)
        op.vincularCosteoVersion(costeoVersionId);

        OrdenProduccionItem itemOP = new OrdenProduccionItem(
                null,
                itemNV.getArticuloId(),
                itemNV.getNroItem(),
                itemNV.getModelo(),
                itemNV.getTela(),
                itemNV.getComposicion(),
                itemNV.getColor(),
                itemNV.getTalla(),
                itemNV.getGenero(),
                itemNV.getCodigo(),
                itemNV.getLlevaLogo(),
                itemNV.getCantidad());
        op.addItem(itemOP);

        OrdenProduccion opPersistida = repository.save(op);

        generarFasesParaItem(opPersistida, notaVenta.getIdNV(), itemNV);

        return opPersistida;
    }

    private void generarFasesParaItem(OrdenProduccion op, Long notaVentaId, ItemNV itemNV) {
        Integer cantidad = itemNV.getCantidad() != null ? itemNV.getCantidad() : 0;
        boolean llevaLogo = itemNV.getLlevaLogo() != null
                && !itemNV.getLlevaLogo().isBlank()
                && !"N/A".equalsIgnoreCase(itemNV.getLlevaLogo())
                && !"NO".equalsIgnoreCase(itemNV.getLlevaLogo());

        java.util.List<FaseProduccion> fases = new java.util.ArrayList<>();
        fases.add(FaseProduccion.CORTE);
        fases.add(FaseProduccion.CONFECCION);
        if (llevaLogo) {
            // La técnica de aplicación del logo (bordado/estampado) se decide a partir de
            // la info de logo del ítem (llevaLogo/logoDetalle), NO del detalle de OT
            // (que describe modificaciones de la prenda, un concepto distinto).
            String infoLogo = ((itemNV.getLlevaLogo() != null ? itemNV.getLlevaLogo() : "") + " "
                    + (itemNV.getLogoDetalle() != null ? itemNV.getLogoDetalle() : "")).toLowerCase();
            if (infoLogo.contains("estampado")) {
                fases.add(FaseProduccion.ESTAMPADO);
            } else {
                fases.add(FaseProduccion.BORDADO);
            }
        }
        fases.add(FaseProduccion.TERMINACION);

        for (FaseProduccion fase : fases) {
            OrdenTrabajo ot = OrdenTrabajo.crearParaFase(
                    notaVentaId,
                    itemNV.getIdItemNV(),
                    op.getIdOP(),
                    itemNV.getNroItem(),
                    fase,
                    cantidad,
                    fase.getDescripcion() + " - " + (itemNV.getModelo() != null ? itemNV.getModelo() : ""));
            otRepository.save(ot);
        }
    }
}
