package backend.com.produccion.application.service;

import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.ItemNV;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.produccion.domain.model.Costeo;
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

    @Transactional
    public OrdenProduccion execute(NotaVenta notaVenta) {
        if (notaVenta == null)
            throw new ValidationException("La Nota de Venta no puede ser nula");

        // Regla: El Nro de la OP se hereda del Nro de Costeo
        DocumentNumber numeroOP = notaVenta.getNumeroNV(); // Fallback por defecto

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
            }
        }

        OrdenProduccion op = OrdenProduccion.crearNueva(
                numeroOP,
                notaVenta.getIdNV(),
                notaVenta.getFechaEntregaEstimada());

        // Mapear ítems de la NV a la OP (solo los que requieren producción)
        if (notaVenta.getItems() != null) {
            for (ItemNV itemNV : notaVenta.getItems()) {
                // Filtro lógico: solo ítems marcados como 'OP'
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

                    // Si el ítem es personalizado, generar la OT
                    if (itemNV.getGeneraOt()) {
                        OrdenTrabajo ot = OrdenTrabajo.crearParaItem(
                                op.getNumeroOP(),
                                notaVenta.getIdNV(),
                                itemNV.getNroItem(),
                                itemNV.getDetalleOt());
                        otRepository.save(ot);
                    }
                }
            }
        }

        return repository.save(op);
    }
}
