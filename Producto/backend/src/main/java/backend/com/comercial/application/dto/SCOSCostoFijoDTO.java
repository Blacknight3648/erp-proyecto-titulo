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
public class SCOSCostoFijoDTO {
    private BigDecimal hilo;
    private BigDecimal manoObraSimple;
    private BigDecimal manoObraGratificacion;
    private BigDecimal etiqueta;
    private BigDecimal embalaje;
    private BigDecimal flete;
    private BigDecimal total;
    private String monedaTotal;
}
