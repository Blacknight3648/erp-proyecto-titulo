package backend.com.comercial.application.UseCase;

import backend.com.comercial.application.dto.CrearNVCommand;
import backend.com.comercial.application.dto.ItemNVDTO;
import backend.com.comercial.application.dto.NVResponse;
import backend.com.comercial.domain.enums.EstadoNV;
import backend.com.comercial.domain.enums.TipoItem;
import backend.com.comercial.domain.model.ItemNV;
import backend.com.comercial.domain.model.ItemNVTalla;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.produccion.application.UseCase.CrearOrdenProduccionUseCase;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActualizarNVUseCase {

    private final NotaVentaRepository nvRepository;
    private final CrearOrdenProduccionUseCase crearOPUseCase;
    private final OrdenProduccionRepository opRepository;

    @Transactional
    public NVResponse ejecutar(Long id, CrearNVCommand command) {
        NotaVenta nv = nvRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota de Venta no encontrada: " + id));

        if (nv.getEstado() != EstadoNV.BORRADOR) {
            throw new IllegalStateException(
                    "Solo las Notas de Venta en BORRADOR pueden ser actualizadas (estado actual: "
                            + nv.getEstado() + ")");
        }

        nv.actualizar(
                command.getClienteId(),
                command.getVendedorId(),
                command.getEsKit(),
                command.getDetalleKit(),
                command.getFechaEntregaEstimada());

        nv.clearItems();

        if (command.getItems() != null) {
            for (int i = 0; i < command.getItems().size(); i++) {
                ItemNVDTO dto = command.getItems().get(i);

                List<ItemNVTalla> tallas = (dto.getTallas() == null) ? null
                        : dto.getTallas().stream()
                                .map(t -> new ItemNVTalla(t.getTalla(), t.getCantidad()))
                                .collect(Collectors.toList());

                ItemNV item = new ItemNV(
                        null,
                        i + 1,
                        dto.getArticuloId(),
                        "Prenda",
                        dto.getModelo(),
                        dto.getTela(),
                        dto.getComposicion(),
                        dto.getColor(),
                        dto.getTalla(),
                        dto.getGenero(),
                        null,
                        dto.getProveedorId(),
                        "PENDIENTE",
                        dto.getLlevaLogo(),
                        dto.getItemType() != null ? dto.getItemType() : TipoItem.OP,
                        dto.getRequiereOt(),
                        dto.getDetalleOt(),
                        dto.getLogoDetalle(),
                        dto.getCantidad(),
                        new Money(dto.getPrecioUnitario(), "CLP"),
                        tallas);
                nv.addItem(item);
            }
        }

        if (Boolean.TRUE.equals(command.getEmitir())) {
            nv.emitir();
        }

        NotaVenta guardada = nvRepository.save(nv);

        if (Boolean.TRUE.equals(command.getEmitir())) {
            boolean tieneItemsOP = guardada.getItems() != null &&
                    guardada.getItems().stream().anyMatch(i -> TipoItem.OP == i.getTipoItem());
            if (tieneItemsOP) {
                List<OrdenProduccion> opsExistentes = opRepository.findByNotaVentaId(guardada.getIdNV());
                Long opId;
                if (!opsExistentes.isEmpty()) {
                    opId = opsExistentes.get(0).getIdOP();
                } else {
                    opId = crearOPUseCase.execute(guardada).getIdOP();
                }
                nvRepository.vincularOpAItems(guardada.getIdNV(), opId);
            }
        }

        return NVResponse.fromDomain(guardada);
    }
}
