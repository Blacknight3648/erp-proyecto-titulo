package backend.com.comercial.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SCOSAccesorioDTO {
    private Long id;
    private String tempId;
    private String tipo;
    private String nombreAccesorio;
    private String proveedorReferencia;
    private Integer cantidad;
    private java.math.BigDecimal consumo;
    private String unidadMedida;
    private java.math.BigDecimal precioUnitario;
    private java.math.BigDecimal precioTotal;
}