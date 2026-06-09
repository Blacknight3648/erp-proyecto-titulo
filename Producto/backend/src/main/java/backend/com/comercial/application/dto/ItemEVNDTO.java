package backend.com.comercial.application.dto;

import backend.com.comercial.domain.model.ItemEspecificacion;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ItemEVNDTO {
    // IDs de relaciones (opcionales)
    private Integer articuloId;
    private Long proveedorId;

    // Datos descriptivos del ítem
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

    // Especificaciones verdaderamente dinámicas (no conocidas de antemano)
    private List<ItemEspecificacion> technicalSpecs;

    // Vínculo con costeo/SCOS — solo si tipoItem = "OP"
    private Long costeoId;
    private Long solicitudCostosId;
}
