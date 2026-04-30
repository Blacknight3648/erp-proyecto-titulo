package backend.com.comercial.domain.model;

import backend.com.shared.valueobjects.Money;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ItemNV {
    private Integer nroItem;
    private Long productoId;
    private String modelo;
    private String tela;
    private String composicion;
    private String color;
    private String talla;
    private String genero;
    private String codigo;
    private Long proveedorId;
    private String llevaLogo;
    private String tipoItem;
    private Boolean generaOt;
    private String detalleOt;
    private String logoDetalle;
    private Integer cantidad;
    private Money precioUnitario;
    private Money total;

    private List<ItemNVTalla> tallas = new ArrayList<>();

    public ItemNV(Integer nroItem, Long productoId, String modelo, String tela, String composicion,
            String color, String talla, String genero, String codigo, Long proveedorId,
            String llevaLogo, String tipoItem, Boolean generaOt, String detalleOt,
            String logoDetalle, Integer cantidad, Money precioUnitario, List<ItemNVTalla> tallas) {
        this.nroItem = nroItem;
        this.productoId = productoId;
        this.modelo = modelo;
        this.tela = tela;
        this.composicion = composicion;
        this.color = color;
        this.talla = talla;
        this.genero = genero;
        this.codigo = codigo;
        this.proveedorId = proveedorId;
        this.llevaLogo = llevaLogo != null ? llevaLogo : "N/A";
        this.tipoItem = tipoItem;
        this.generaOt = generaOt != null ? generaOt : false;
        this.detalleOt = detalleOt;
        this.logoDetalle = logoDetalle;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        if (tallas != null) {
            this.tallas.addAll(tallas);
        }
        calcularTotal();
    }

    private void calcularTotal() {
        if (this.precioUnitario != null && this.cantidad != null) {
            this.total = this.precioUnitario.multiply(this.cantidad);
        } else {
            this.total = new Money(BigDecimal.ZERO, "CLP");
        }
    }

    public List<ItemNVTalla> getTallas() {
        return Collections.unmodifiableList(tallas);
    }
}
