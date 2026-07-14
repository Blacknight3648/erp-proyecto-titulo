package backend.com.comercial.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SCOSTelaDTO {
    private Long idSCOSTela;
    private String tempId;
    private String aplicacion;
    private String nombre;
    private String proveedorReferencia;
    private String composicion;
    private String color;
    private java.math.BigDecimal peso;
    private java.math.BigDecimal consumo;
    private String unidadMedida;
    private java.math.BigDecimal precioUnitario;
    private java.math.BigDecimal precioTotal;
}