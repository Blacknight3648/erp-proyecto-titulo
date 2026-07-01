package backend.com.produccion.application.UseCase;

import backend.com.produccion.application.dto.ActualizarHojaCompraItemRequest;
import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModificarHojaCompraItemUseCase {

    private final HojaCompraRepository hojaCompraRepository;

    @Transactional
    public HojaCompra ejecutar(Long idHC, Long idHCItem, ActualizarHojaCompraItemRequest request) {
        HojaCompra hc = hojaCompraRepository.findById(idHC)
                .orElseThrow(() -> new EntityNotFoundException("Hoja de Compra no encontrada: " + idHC));

        if (hc.getEstado() == EstadoHC.CERRADA) {
            throw new BusinessRuleException("No se puede modificar un ítem en una Hoja de Compra CERRADA");
        }

        HojaCompraItem itemTarget = null;
        if (hc.getItems() != null) {
            for (HojaCompraItem item : hc.getItems()) {
                if (item.getIdHCItem() != null && item.getIdHCItem().equals(idHCItem)) {
                    itemTarget = item;
                    break;
                }
            }
        }

        if (itemTarget == null) {
            throw new EntityNotFoundException("Ítem de Hoja de Compra no encontrado con ID: " + idHCItem);
        }

        if (itemTarget.getOcId() != null) {
            throw new BusinessRuleException("No se puede modificar un ítem que ya fue vinculado a una Orden de Compra");
        }

        itemTarget.modificar(request.getCantidadAComprar(), request.getPrecioUnitarioRef(), request.getJustificacionModificacion(), request.getCantidadStock());

        return hojaCompraRepository.save(hc);
    }
}
