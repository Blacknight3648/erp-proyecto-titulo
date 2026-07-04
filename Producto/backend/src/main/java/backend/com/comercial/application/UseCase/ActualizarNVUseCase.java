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
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActualizarNVUseCase {

    private final NotaVentaRepository nvRepository;
    private final CrearOrdenProduccionUseCase crearOPUseCase;
    private final NumeroDocumentoService numeroDocumentoService;

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

        // Los ítems que ya existían pueden tener una OP asignada y/o un número de OP
        // reservado; hay que preservarlos al reconstruir la lista desde el comando.
        Map<Long, ItemNV> itemsActualesPorId = nv.getItems() == null ? Map.of()
                : nv.getItems().stream()
                        .filter(i -> i.getIdItemNV() != null)
                        .collect(Collectors.toMap(ItemNV::getIdItemNV, i -> i));

        nv.clearItems();

        if (command.getItems() != null) {
            for (int i = 0; i < command.getItems().size(); i++) {
                ItemNVDTO dto = command.getItems().get(i);

                List<ItemNVTalla> tallas = (dto.getTallas() == null) ? null
                        : dto.getTallas().stream()
                                .map(t -> new ItemNVTalla(t.getTalla(), t.getCantidad()))
                                .collect(Collectors.toList());

                ItemNV item = new ItemNV(
                        dto.getIdItemNV(),
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

                ItemNV existente = dto.getIdItemNV() != null ? itemsActualesPorId.get(dto.getIdItemNV()) : null;
                if (existente != null && existente.getOpId() != null) {
                    item.vincularOp(existente.getOpId());
                } else if (dto.getCosteoId() != null) {
                    item.setCosteoIdManual(dto.getCosteoId());
                }

                // Preservar número de OP reservado del guardado anterior.
                if (existente != null && existente.getNumeroOPReservado() != null) {
                    item.setNumeroOPReservado(existente.getNumeroOPReservado());
                }

                nv.addItem(item);
            }
        }

        if (Boolean.TRUE.equals(command.getEmitir())) {
            nv.emitir();
        }

        // Borrador: reservar números de OP para ítems OP nuevos que aún no tienen número.
        if (!Boolean.TRUE.equals(command.getEmitir())) {
            if (nv.getItems() != null) {
                for (ItemNV item : nv.getItems()) {
                    if (TipoItem.OP == item.getTipoItem()
                            && item.getNumeroOPReservado() == null
                            && item.getOpId() == null) {
                        DocumentNumber reservado = numeroDocumentoService.siguienteFormateado("OP");
                        item.setNumeroOPReservado(reservado.getValue());
                    }
                }
            }
        }

        NotaVenta guardada = nvRepository.save(nv);

        if (Boolean.TRUE.equals(command.getEmitir())) {
            crearOPUseCase.execute(guardada);
            // Re-leer desde BD para que el NVResponse incluya los opId asignados.
            guardada = nvRepository.findById(guardada.getIdNV()).orElse(guardada);
        }

        return NVResponse.fromDomain(guardada);
    }
}
