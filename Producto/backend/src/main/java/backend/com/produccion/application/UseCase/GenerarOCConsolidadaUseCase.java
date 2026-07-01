package backend.com.produccion.application.UseCase;

import backend.com.produccion.application.dto.GenerarOCConsolidadaRequest;
import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.produccion.domain.model.HCItemOCItemLink;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.model.OrdenCompraItem;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.ValidationException;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenerarOCConsolidadaUseCase {

    private final HojaCompraRepository hojaCompraRepository;
    private final OrdenCompraRepository ordenCompraRepository;

    @Transactional
    public OrdenCompra ejecutar(GenerarOCConsolidadaRequest request) {
        OrdenCompra oc = ordenCompraRepository.save(construirOC(request));
        vincularItemsAOrdenCompra(oc, request.getHcItemIds());
        return oc;
    }

    public void vincularItemsAOrdenCompra(OrdenCompra oc, List<Long> hcItemIds) {
        if (oc == null || oc.getIdOC() == null || hcItemIds == null || hcItemIds.isEmpty()) {
            return;
        }
        Set<Long> idsSet = new HashSet<>(hcItemIds);
        List<HojaCompra> hcs = hojaCompraRepository.findAllByItemIds(hcItemIds);
        String numOC = oc.getNumeroOC() != null ? oc.getNumeroOC().getValue() : String.valueOf(oc.getIdOC());

        for (HojaCompra hc : hcs) {
            boolean modificado = false;
            for (HojaCompraItem item : hc.getItems()) {
                if (idsSet.contains(item.getIdHCItem())) {
                    item.vincularOC(oc.getIdOC(), numOC);
                    modificado = true;
                }
            }
            if (modificado) {
                hojaCompraRepository.save(hc);
            }
        }
    }

    /**
     * Construye la OrdenCompra a partir de un grupo (proveedor + hcItemIds) sin
     * persistirla, para permitir que GenerarOCLoteUseCase la combine con otras
     * OC dentro de una única transacción atómica.
     */
    OrdenCompra construirOC(GenerarOCConsolidadaRequest request) {
        validar(request);

        Set<Long> hcItemIdsSet = new HashSet<>(request.getHcItemIds());
        List<HojaCompra> hcs = hojaCompraRepository.findAllByItemIds(request.getHcItemIds());

        if (hcs.isEmpty()) {
            throw new BusinessRuleException(
                    "No se encontraron HojasCompra con los hcItemIds dados");
        }

        // Todas las HCs involucradas deben estar APROBADAS
        for (HojaCompra hc : hcs) {
            if (hc.getEstado() != EstadoHC.APROBADA) {
                throw new BusinessRuleException(
                        "La HC " + hc.getIdHC() + " no está APROBADA (estado actual: " + hc.getEstado() + ")");
            }
        }

        // Recolectar los HCItems que efectivamente vienen en el request
        List<HojaCompraItem> itemsSeleccionados = new ArrayList<>();
        for (HojaCompra hc : hcs) {
            for (HojaCompraItem item : hc.getItems()) {
                if (hcItemIdsSet.contains(item.getIdHCItem())) {
                    if (item.getOcId() != null) {
                        throw new BusinessRuleException(
                                "El ítem '" + item.getNombreInsumo() + "' ya está vinculado a la OC #" + item.getOcId() + " (" + item.getNumeroOC() + ")");
                    }
                    itemsSeleccionados.add(item);
                }
            }
        }

        // Sanity check: ¿se encontraron todos los hcItemIds pedidos?
        Set<Long> encontrados = new HashSet<>();
        itemsSeleccionados.forEach(i -> encontrados.add(i.getIdHCItem()));
        for (Long pedido : hcItemIdsSet) {
            if (!encontrados.contains(pedido)) {
                throw new BusinessRuleException("No se encontró el hcItemId: " + pedido);
            }
        }

        // Agrupar por (tipoInsumo, articuloId)
        Map<String, List<HojaCompraItem>> agrupados = new LinkedHashMap<>();
        for (HojaCompraItem item : itemsSeleccionados) {
            String key = item.getTipoInsumo() + "|" + item.getArticuloId();
            agrupados.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }

        // Construir OC
        OrdenCompra oc = OrdenCompra.emitir(
                construirNumeroOC(request.getProveedorId()),
                request.getProveedorId(),
                request.getFechaEntregaEstimada(),
                request.getObservaciones());

        for (Map.Entry<String, List<HojaCompraItem>> entry : agrupados.entrySet()) {
            List<HojaCompraItem> grupo = entry.getValue();
            HojaCompraItem ref = grupo.get(0);

            BigDecimal cantidadRequerida = grupo.stream()
                    .map(HojaCompraItem::getCantidadRequerida)
                    .filter(v -> v != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal cantidadStock = BigDecimal.ZERO; // preparado para futuro stock
            BigDecimal cantidadComprada = cantidadRequerida.subtract(cantidadStock);
            BigDecimal precioUnitario = ref.getPrecioUnitarioRef() != null
                    ? ref.getPrecioUnitarioRef()
                    : BigDecimal.ZERO;
            BigDecimal subtotal = cantidadComprada.multiply(precioUnitario);

            List<HCItemOCItemLink> links = new ArrayList<>();
            for (HojaCompraItem origen : grupo) {
                links.add(new HCItemOCItemLink(
                        origen.getIdHCItem(),
                        null,
                        origen.getCantidadRequerida()));
            }

            OrdenCompraItem ocItem = new OrdenCompraItem(
                    null, null,
                    ref.getTipoInsumo(),
                    ref.getArticuloId(),
                    ref.getNombreInsumo(),
                    cantidadRequerida,
                    cantidadStock,
                    cantidadComprada,
                    precioUnitario,
                    subtotal,
                    links);

            oc.addItem(ocItem);
        }

        return oc;
    }

    private void validar(GenerarOCConsolidadaRequest request) {
        if (request == null) {
            throw new ValidationException("El request no puede ser nulo");
        }
        if (request.getProveedorId() == null) {
            throw new ValidationException("El proveedorId es obligatorio");
        }
        if (request.getHcItemIds() == null || request.getHcItemIds().isEmpty()) {
            throw new ValidationException("Debe enviar al menos un hcItemId");
        }
    }

    private DocumentNumber construirNumeroOC(Long proveedorId) {
        return new DocumentNumber("OC-" + proveedorId + "-" + System.currentTimeMillis());
    }
}
