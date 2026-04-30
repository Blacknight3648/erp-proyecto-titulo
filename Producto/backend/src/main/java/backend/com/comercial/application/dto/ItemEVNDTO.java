package backend.com.comercial.application.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemEVNDTO {
    // IDs de relaciones (opcionales)
    private Long productoId;
    private Long proveedorId;

    // Datos descriptivos del ítem (enviados desde el frontend)
    private Integer nroItem;
    private String descripcion;
    private String modelo;
    private String tela;
    private String composicion;
    private String genero;
    private String codigoInterno;
    private String codigoProveedor;
    private String proveedorNombre;

    // Cantidades y precios
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal costoUnitario;

    // Costos individuales del ítem
    private BigDecimal costoProducto;
    private BigDecimal costoLogo;
    private BigDecimal costoOrdenTrabajo;

    // Clasificación
    private String tipoItem;
    private java.util.Map<String, String> technicalSpecs;
}
