package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Precio unitario pagado por un insumo en una OC puntual, para el análisis
 * comparativo de precios históricos por producto y proveedor.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialPrecioDTO {
    private String numeroOC;
    private LocalDate fechaEmision;
    private Integer articuloId;
    private String nombreInsumo;
    private Long proveedorId;
    private String proveedorNombre;
    private BigDecimal precioUnitario;
}
