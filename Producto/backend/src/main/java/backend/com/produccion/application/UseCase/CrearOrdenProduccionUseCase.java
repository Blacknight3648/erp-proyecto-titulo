package backend.com.produccion.application.UseCase;

import backend.com.comercial.domain.enums.TipoItem;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.ItemNV;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
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

@Service
@RequiredArgsConstructor
public class CrearOrdenProduccionUseCase {

    private final OrdenProduccionRepository repository;
    private final EvaluacionNegocioRepository evnRepository;
    private final CosteoRepository costeoRepository;
    private final OrdenTrabajoRepository otRepository;
    private final CrearVersionCosteoUseCase crearVersionCosteoUseCase;
    private final NumeroDocumentoService numeroDocumentoService;

    @Transactional
    public OrdenProduccion execute(NotaVenta notaVenta) {
        if (notaVenta == null)
            throw new ValidationException("La Nota de Venta no puede ser nula");

        // Req 1: una NV solo puede tener una OP. Si ya existe, rechazar la creación.
        boolean opExistente = !repository.findByNotaVentaId(notaVenta.getIdNV()).isEmpty();
        if (opExistente) {
            throw new ValidationException(
                    "La NV " + notaVenta.getIdNV() + " ya tiene una Orden de Producción asociada. " +
                    "No se puede crear una segunda OP para la misma NV.");
        }

        boolean tieneItemsOP = notaVenta.getItems() != null &&
                notaVenta.getItems().stream()
                        .anyMatch(i -> backend.com.comercial.domain.enums.TipoItem.OP == i.getTipoItem());

        if (!tieneItemsOP) {
            throw new ValidationException(
                    "La NV " + notaVenta.getIdNV() + " no contiene ítems de tipo OP; no se puede crear una OP.");
        }

        DocumentNumber numeroOP = numeroDocumentoService.siguienteFormateado("OP");
        Long costeoVersionId = null;

        // Intento 1: buscar costeo vinculado en los ítems OP de la EVN plantilla
        if (notaVenta.getEvaluacionNegocioId() != null) {
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

        // Intento 2 (garantía ACID): si la EVN no tenía costeo vinculado, crear uno vacío
        // vinculado a la NV. La restricción UNIQUE(nota_venta_id) en produccion_costeos
        // garantiza idempotencia — no se crea un duplicado si se llama dos veces.
        if (costeoVersionId == null) {
            Costeo costeoBase = costeoRepository.findByNotaVentaId(notaVenta.getIdNV())
                    .orElseGet(() -> {
                        DocumentNumber numeroCosteo = numeroDocumentoService.siguienteFormateado("COST");
                        Costeo vacio = Costeo.crearVacio(numeroCosteo, notaVenta.getIdNV());
                        return costeoRepository.save(vacio);
                    });

            CosteoVersion version = crearVersionCosteoUseCase.ejecutar(
                    costeoBase.getIdCosteo(),
                    "Costeo inicial auto-creado — NV sin costeo pre-vinculado en EVN",
                    "SYSTEM");
            costeoVersionId = version.getIdCosteoVersion();
        }

        OrdenProduccion op = OrdenProduccion.crearNueva(
                numeroOP,
                notaVenta.getIdNV(),
                notaVenta.getFechaEntregaEstimada());

        // costeoVersionId siempre es no-nulo en este punto (invariante garantizado)
        op.vincularCosteoVersion(costeoVersionId);

        // Mapear ítems de la NV a la OP (solo los que requieren producción)
        if (notaVenta.getItems() != null) {
            for (ItemNV itemNV : notaVenta.getItems()) {
                if (TipoItem.OP == itemNV.getTipoItem()) {
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
                }
            }
        }

        OrdenProduccion opPersistida = repository.save(op);

        // Generación automática de fases (OT) por cada ítem productivo
        if (notaVenta.getItems() != null) {
            for (ItemNV itemNV : notaVenta.getItems()) {
                if (TipoItem.OP != itemNV.getTipoItem()) {
                    continue;
                }
                generarFasesParaItem(opPersistida, notaVenta.getIdNV(), itemNV);
            }
        }

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
