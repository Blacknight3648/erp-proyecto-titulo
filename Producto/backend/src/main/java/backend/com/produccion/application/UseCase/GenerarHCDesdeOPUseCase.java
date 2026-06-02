package backend.com.produccion.application.UseCase;

import backend.com.produccion.domain.model.CosteoVersion;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.model.OrdenProduccionItem;
import backend.com.produccion.domain.repository.CosteoVersionRepository;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenerarHCDesdeOPUseCase {

    private final OrdenProduccionRepository ordenProduccionRepository;
    private final CosteoVersionRepository costeoVersionRepository;
    private final HojaCompraRepository hojaCompraRepository;

    @Transactional
    public HojaCompra ejecutar(Long opId) {
        if (opId == null) {
            throw new ValidationException("El opId es obligatorio para generar la HC");
        }

        OrdenProduccion op = ordenProduccionRepository.findById(opId)
                .orElseThrow(() -> new EntityNotFoundException("Orden de Producción no encontrada: " + opId));

        if (op.getCosteoVersionId() == null) {
            throw new BusinessRuleException(
                    "La OP " + opId + " no tiene una versión de Costeo vinculada; no se puede generar la HC");
        }

        if (hojaCompraRepository.existsByOpId(opId)) {
            throw new BusinessRuleException(
                    "Ya existe una Hoja de Compra para la OP " + opId);
        }

        CosteoVersion costeoVersion = costeoVersionRepository.findById(op.getCosteoVersionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Versión de Costeo no encontrada: " + op.getCosteoVersionId()));

        Integer cantidadTotalOP = calcularCantidadTotal(op);

        DocumentNumber numeroHC = construirNumeroHC(op);

        HojaCompra hc = HojaCompra.crearBorrador(numeroHC, opId, op.getCosteoVersionId());

        if (costeoVersion.getItems() != null) {
            costeoVersion.getItems().forEach(itemVersion -> {
                HojaCompraItem hcItem = HojaCompraItem.desdeCosteoVersionItem(itemVersion, cantidadTotalOP);
                hc.addItem(hcItem);
            });
        }

        return hojaCompraRepository.save(hc);
    }

    private Integer calcularCantidadTotal(OrdenProduccion op) {
        if (op.getItems() == null)
            return 0;
        return op.getItems().stream()
                .map(OrdenProduccionItem::getCantidad)
                .filter(c -> c != null)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private DocumentNumber construirNumeroHC(OrdenProduccion op) {
        String base = op.getNumeroOP() != null ? op.getNumeroOP().getValue() : ("OP-" + op.getIdOP());
        return new DocumentNumber("HC-" + base);
    }
}
