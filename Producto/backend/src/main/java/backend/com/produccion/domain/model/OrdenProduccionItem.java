package backend.com.produccion.domain.model;

import lombok.Getter;

@Getter
public class OrdenProduccionItem {
    private Long idOPItem;
    private Long productoId;
    private Integer nroItem;
    private String modelo;
    private String tela;
    private String composicion;
    private String color;
    private String talla;
    private String genero;
    private String codigo;
    private String llevaLogo;
    private Integer cantidad;

    public OrdenProduccionItem(Long id, Long productoId, Integer nroItem, String modelo, String tela,
            String composicion, String color, String talla, String genero,
            String codigo, String llevaLogo, Integer cantidad) {
        this.idOPItem = id;
        this.productoId = productoId;
        this.nroItem = nroItem;
        this.modelo = modelo;
        this.tela = tela;
        this.composicion = composicion;
        this.color = color;
        this.talla = talla;
        this.genero = genero;
        this.codigo = codigo;
        this.llevaLogo = llevaLogo != null ? llevaLogo : "N/A";
        this.cantidad = cantidad;
    }
}
