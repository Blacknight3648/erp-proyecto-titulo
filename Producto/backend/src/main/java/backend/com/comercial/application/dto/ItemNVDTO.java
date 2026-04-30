package backend.com.comercial.application.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ItemNVDTO {

    private Integer nroItem;
    private Long productoId;
    private Integer cantidad; // Restaurado
    private String modelo;
    private String tela;
    private String composicion;
    private String color;
    private String talla;
    private String genero;
    private Long proveedorId;
    private String llevaLogo;
    private String logoDetalle;
    private String itemType; // OP, SC, o SCI
    private Boolean generaOt;
    private String detalleOt;
    private List<TallaDTO> tallas;
    private BigDecimal precioUnitario;
    private BigDecimal total;

    @Data
    public static class TallaDTO {
        private String talla;
        private Integer cantidad;
    }
}
