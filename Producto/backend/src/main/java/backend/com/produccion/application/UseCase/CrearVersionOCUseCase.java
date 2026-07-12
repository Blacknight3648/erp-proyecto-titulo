package backend.com.produccion.application.UseCase;

import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.model.OrdenCompraItem;
import backend.com.produccion.domain.model.OrdenCompraItemVersion;
import backend.com.produccion.domain.model.OrdenCompraVersion;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.produccion.domain.repository.OrdenCompraVersionRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Congela un snapshot de la OrdenCompra (cabecera + ítems) tal como está en
 * el momento de invocarse. Análogo a {@code CrearVersionCosteoUseCase}: se
 * usa antes de rechazar una OC, para no perder proveedor/cantidades/precios
 * previos cuando un reingreso posterior los sobrescriba.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CrearVersionOCUseCase {

    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenCompraVersionRepository ordenCompraVersionRepository;

    public OrdenCompraVersion ejecutar(Long ocId, String motivoCambio, String usuarioCreador) {
        if (ocId == null) {
            throw new ValidationException("El ocId es obligatorio para crear una versión");
        }

        OrdenCompra oc = ordenCompraRepository.findById(ocId)
                .orElseThrow(() -> new ValidationException("No existe la Orden de Compra con id: " + ocId));

        Integer numeroVersion = ordenCompraVersionRepository.siguienteNumeroVersion(ocId);

        List<OrdenCompraItemVersion> snapshotItems = oc.getItems() != null
                ? oc.getItems().stream()
                        .map(this::snapshotItem)
                        .collect(Collectors.toList())
                : List.of();

        OrdenCompraVersion version = new OrdenCompraVersion(
                null,
                ocId,
                numeroVersion,
                LocalDateTime.now(),
                motivoCambio,
                usuarioCreador,
                snapshotItems,
                oc.getProveedorId(),
                oc.getFechaEntregaEstimada(),
                oc.getObservaciones(),
                oc.getTotalNeto());

        return ordenCompraVersionRepository.save(version);
    }

    private OrdenCompraItemVersion snapshotItem(OrdenCompraItem origen) {
        return new OrdenCompraItemVersion(
                null,
                null,
                origen.getIdOCItem(),
                origen.getTipoInsumo(),
                origen.getArticuloId(),
                origen.getNombreInsumo(),
                origen.getCantidadComprada(),
                origen.getPrecioUnitario(),
                origen.getSubtotal());
    }
}
