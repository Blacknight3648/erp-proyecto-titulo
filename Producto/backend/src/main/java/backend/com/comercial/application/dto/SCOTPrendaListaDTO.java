package backend.com.comercial.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SCOTPrendaListaDTO {
    private String nombre;
    private Integer cantidad;
    private String talla;
    private String color;
    private String proveedorReferencia;
    private String linkReferencia;
    private String composicion;
    private String peso;
    private String observaciones;
    private Double precioUnitario;
    private Double costoTotal;
}
