package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdenCompraItemDTO {

    private Long idOCItem;
    private Long ocId;
    private String tipoInsumo;
    private Integer articuloId;
    private String nombreInsumo;
    private BigDecimal cantidadRequerida;
    private BigDecimal cantidadStock;
    private BigDecimal cantidadComprada;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private List<HCItemOCItemLinkDTO> hcLinks;
}
