package backend.com.comercial.application.dto;

import backend.com.comercial.domain.enums.TipoItem;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ItemNVDTO {

    /**
     * Id del ítem ya persistido (al editar una NV existente). Null para ítems nuevos —
     * permite distinguir "ítem que ya tenía una OP asociada" de "ítem nuevo sin OP".
     */
    private Long idItemNV;
    private Integer nroItem;
    private Integer articuloId;
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
    private TipoItem itemType;
    /**
     * Costeo existente (APROBADO) a vincular manualmente a la OP de este ítem, cuando
     * es un ítem tipo OP nuevo que no hereda un costeo desde la EVN plantilla.
     */
    private Long costeoId;
    private Boolean requiereOt;
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
