package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Vista compacta de una versión de Orden de Compra para mostrar en el
 * historial de rechazo/reingreso. Análoga a HistorialVersionCosteoDTO.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistorialVersionOCDTO {
    private String id;
    private Integer version;
    private String accion; // EMISION, RECHAZO, REINGRESO
    private String fechaFormateada;
    private BigDecimal totalNeto;
    private BigDecimal totalAnterior;
    private Long proveedorId;
    private String usuario;
    private String motivo;
}
