package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.RecepcionOCDTO;
import backend.com.produccion.application.dto.RecepcionOCItemDTO;
import backend.com.produccion.domain.model.EstadoOC;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.model.OrdenCompraItem;
import backend.com.produccion.domain.model.RecepcionOC;
import backend.com.produccion.domain.model.RecepcionOCItem;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.produccion.domain.repository.RecepcionOCRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrarRecepcionOCUseCase {

    private final OrdenCompraRepository ordenCompraRepository;
    private final RecepcionOCRepository recepcionOCRepository;

    @Transactional
    public RecepcionOC ejecutar(Long ocId, RecepcionOCDTO request) {
        validar(ocId, request);

        OrdenCompra oc = ordenCompraRepository.findById(ocId)
                .orElseThrow(() -> new EntityNotFoundException("Orden de Compra no encontrada: " + ocId));

        if (oc.getEstado() != EstadoOC.ENVIADA && oc.getEstado() != EstadoOC.RECEPCIONADA_PARCIAL) {
            throw new BusinessRuleException(
                    "Solo se puede recepcionar una OC ENVIADA o RECEPCIONADA_PARCIAL (estado actual: " + oc.getEstado() + ")");
        }

        // ocItemId -> cantidadComprada
        Map<Long, BigDecimal> compradoPorItem = new HashMap<>();
        for (OrdenCompraItem item : oc.getItems()) {
            compradoPorItem.put(item.getIdOCItem(), item.getCantidadComprada());
        }

        // Cantidades ya recibidas históricas
        Map<Long, BigDecimal> recibidoAcumulado = new HashMap<>(
                recepcionOCRepository.sumarRecibidoPorOCItem(ocId));

        // Validar que cada item del request exista en la OC y no exceda lo comprado
        for (RecepcionOCItemDTO itemDTO : request.getItems()) {
            BigDecimal cantidadComprada = compradoPorItem.get(itemDTO.getOcItemId());
            if (cantidadComprada == null) {
                throw new BusinessRuleException(
                        "El OCItem " + itemDTO.getOcItemId() + " no pertenece a la OC " + ocId);
            }
            BigDecimal yaRecibido = recibidoAcumulado.getOrDefault(itemDTO.getOcItemId(), BigDecimal.ZERO);
            BigDecimal nuevaCantidad = itemDTO.getCantidadRecibida() != null
                    ? itemDTO.getCantidadRecibida() : BigDecimal.ZERO;
            BigDecimal totalProyectado = yaRecibido.add(nuevaCantidad);
            if (totalProyectado.compareTo(cantidadComprada) > 0) {
                throw new BusinessRuleException(
                        "El total recibido (" + totalProyectado + ") para el OCItem " + itemDTO.getOcItemId()
                                + " excede lo comprado (" + cantidadComprada + ")");
            }
            // Actualizar acumulado para verificación posterior del estado
            recibidoAcumulado.put(itemDTO.getOcItemId(), totalProyectado);
        }

        // Construir entidad de dominio Recepcion
        RecepcionOC recepcion = RecepcionOC.crear(
                ocId,
                request.getFechaRecepcion(),
                request.getNumeroGuia(),
                request.getResponsable(),
                request.getObservaciones());

        for (RecepcionOCItemDTO itemDTO : request.getItems()) {
            recepcion.addItem(new RecepcionOCItem(
                    null,
                    null,
                    itemDTO.getOcItemId(),
                    itemDTO.getCantidadRecibida(),
                    itemDTO.getCantidadConforme(),
                    itemDTO.getCantidadRechazada(),
                    itemDTO.getMotivoRechazo()));
        }

        RecepcionOC saved = recepcionOCRepository.save(recepcion);

        // Calcular nuevo estado de la OC
        EstadoOC nuevoEstado = calcularNuevoEstado(compradoPorItem, recibidoAcumulado);
        if (nuevoEstado != oc.getEstado()) {
            transicionar(oc, nuevoEstado);
            ordenCompraRepository.save(oc);
        }

        return saved;
    }

    private EstadoOC calcularNuevoEstado(Map<Long, BigDecimal> compradoPorItem,
                                         Map<Long, BigDecimal> recibidoAcumulado) {
        boolean todosCompletos = true;
        boolean algoRecibido = false;
        for (Map.Entry<Long, BigDecimal> entry : compradoPorItem.entrySet()) {
            BigDecimal comprado = entry.getValue();
            BigDecimal recibido = recibidoAcumulado.getOrDefault(entry.getKey(), BigDecimal.ZERO);
            if (recibido.signum() > 0) algoRecibido = true;
            if (recibido.compareTo(comprado) < 0) todosCompletos = false;
        }
        if (todosCompletos) return EstadoOC.RECEPCIONADA;
        if (algoRecibido) return EstadoOC.RECEPCIONADA_PARCIAL;
        return EstadoOC.ENVIADA;
    }

    private void transicionar(OrdenCompra oc, EstadoOC destino) {
        switch (destino) {
            case RECEPCIONADA_PARCIAL -> oc.marcarRecepcionParcial();
            case RECEPCIONADA          -> oc.marcarRecepcionada();
            default                    -> { /* no-op */ }
        }
    }

    private void validar(Long ocId, RecepcionOCDTO request) {
        if (ocId == null) {
            throw new ValidationException("El ocId es obligatorio");
        }
        if (request == null) {
            throw new ValidationException("El request no puede ser nulo");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ValidationException("Debe registrar al menos un item recibido");
        }
    }
}
