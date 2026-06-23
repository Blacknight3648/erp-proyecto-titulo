package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HojaCompraItemDTO {

    private Long idHCItem;
    private Long hcId;
    private String tipoInsumo;
    private Integer articuloId;
    private String nombreInsumo;
    private BigDecimal consumoUnitario;
    private Integer cantidadOP;
    private BigDecimal cantidadRequerida;
    private BigDecimal precioUnitarioRef;
    
    // Nuevos campos para proveedor y OC vinculada
    private Long proveedorId;
    private String proveedorNombre;
    private Long ocId;
    private String numeroOC;
}
