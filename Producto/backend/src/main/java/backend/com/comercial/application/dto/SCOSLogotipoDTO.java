package backend.com.comercial.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SCOSLogotipoDTO {
    private Long idSCOSLogotipo;
    private String tipo;
    private String nombre;
    private String ubicacion;
    private String color;
    private String tamanio;
    private Integer cantidad;
    private BigDecimal precio;
}
