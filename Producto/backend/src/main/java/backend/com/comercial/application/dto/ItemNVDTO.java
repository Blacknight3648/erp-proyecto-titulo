package backend.com.comercial.application.dto;

import backend.com.comercial.domain.enums.TipoItem;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ItemNVDTO {

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
     * Costeo existente (APROBADO) a vincular manualmente a la OP, para ítems tipo OP
     * que no heredan un costeo desde la EVN plantilla. Solo aplica al crear la NV.
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
