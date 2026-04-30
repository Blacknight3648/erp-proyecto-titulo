package backend.com.comercial.domain.model;

import backend.com.shared.valueobjects.Money;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class SCOTPrendaLista {
    private Long SCOTPrendaListaId;
    private String nombre;
    private Integer cantidad;
    private String talla;
    private String color;
    private String proveedorReferencia;
    private String linkReferencia;
    private String composicion;
    private String peso;
    private String observaciones;
    private Money precioUnitario;

    public SCOTPrendaLista(Long SCOTPrendaListaId, String nombre, Integer cantidad, String talla, String color,
            String proveedorReferencia, String linkReferencia, String composicion,
            String peso, String observaciones, Money precioUnitario) {
        this.SCOTPrendaListaId = SCOTPrendaListaId;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.talla = talla;
        this.color = color;
        this.proveedorReferencia = proveedorReferencia;
        this.linkReferencia = linkReferencia;
        this.composicion = composicion;
        this.peso = peso;
        this.observaciones = observaciones;
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        if (this.precioUnitario == null || this.cantidad == null) {
            return BigDecimal.ZERO;
        }
        return this.precioUnitario.getAmount().multiply(new BigDecimal(this.cantidad));
    }
}
