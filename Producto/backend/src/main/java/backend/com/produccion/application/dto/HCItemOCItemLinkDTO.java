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
public class HCItemOCItemLinkDTO {

    private Long hcItemId;
    private Long ocItemId;
    private BigDecimal cantidadAsignada;
}
