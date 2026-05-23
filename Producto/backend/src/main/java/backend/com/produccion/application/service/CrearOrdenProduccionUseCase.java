package backend.com.produccion.application.service;

import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.ItemNV;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.CosteoVersion;
import backend.com.produccion.domain.model.FaseProduccion;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.model.OrdenProduccionItem;
import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
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

    @Transactional
    public OrdenProduccion execute(NotaVenta notaVenta) {
        if (notaVenta == null)
            throw new ValidationException("La Nota de Venta no puede ser nula");

        // Regla: El Nro de la OP se hereda del Nro de Costeo
        DocumentNumber numeroOP = notaVenta.getNumeroNV(); // Fallback por defecto
        Long costeoVersionId = null;

        if (notaVenta.getEvaluacionNegocioId() != null) {
            EvaluacionNegocio evn = evnRepository.findById(notaVenta.getEvaluacionNegocioId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Evaluación de Negocio no encontrada: " + notaVenta.getEvaluacionNegocioId()));

            if (evn.getCosteoId() != null) {
                Costeo costeo = costeoRepository.findById(evn.getCosteoId())
                        .orElseThrow(() -> new EntityNotFoundException("Costeo no encontrado: " + evn.getCosteoId()));

                if (costeo.getNumeroCosteo() != null) {
                    numeroOP = costeo.getNumeroCosteo();
                }

                // Snapshot inicial del Costeo: la OP queda anclada a esta versión
                CosteoVersion versionInicial = crearVersionCosteoUseCase.ejecutar(
                        costeo.getIdCosteo(),
                        "Versión inicial al crear OP",
                        "SYSTEM");
                costeoVersionId = versionInicial.getIdCosteoVersion();
            }
        }

        OrdenProduccion op = OrdenProduccion.crearNueva(
                numeroOP,
                notaVenta.getIdNV(),
                notaVenta.getFechaEntregaEstimada());

        if (costeoVersionId != null) {
            op.vincularCosteoVersion(costeoVersionId);
        }

        // Mapear ítems de la NV a la OP (solo los que requieren producción)
        if (notaVenta.getItems() != null) {
            for (ItemNV itemNV : notaVenta.getItems()) {
                if ("OP".equalsIgnoreCase(itemNV.getTipoItem())) {
                    OrdenProduccionItem itemOP = new OrdenProduccionItem(
                            null,
                            itemNV.getProductoId(),
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
                if (!"OP".equalsIgnoreCase(itemNV.getTipoItem())) {
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
            // Si la marca explícita lo indica, asignamos bordado por defecto;
            // si el detalle menciona "estampado" se agrega estampado en su lugar.
            String detalle = (itemNV.getDetalleOt() != null ? itemNV.getDetalleOt() : "").toLowerCase();
            if (detalle.contains("estampado")) {
                fases.add(FaseProduccion.ESTAMPADO);
            } else {
                fases.add(FaseProduccion.BORDADO);
            }
        }
        fases.add(FaseProduccion.TERMINACION);

        int seq = 1;
        for (FaseProduccion fase : fases) {
            String numeroOT = (op.getNumeroOP() != null ? op.getNumeroOP().getValue() : "OT")
                    + "-I" + itemNV.getNroItem() + "-F" + (seq++);
            OrdenTrabajo ot = OrdenTrabajo.crearParaFase(
                    new backend.com.shared.valueobjects.DocumentNumber(numeroOT),
                    notaVentaId,
                    op.getIdOP(),
                    itemNV.getNroItem(),
                    fase,
                    cantidad,
                    fase.getDescripcion() + " - " + (itemNV.getModelo() != null ? itemNV.getModelo() : ""));
            otRepository.save(ot);
        }
    }
}
