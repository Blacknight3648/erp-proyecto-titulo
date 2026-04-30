package backend.com.comercial.domain.model;

import backend.com.shared.exception.EVNBusinessException;
import backend.com.shared.valueobjects.Money;
import lombok.Value;

@Value
public class ItemEVN {
    Long productoId;
    Long proveedorId;
    Integer cantidad;
    Money precioUnitario;
    Money costoUnitario;
    java.math.BigDecimal costoProducto;
    java.math.BigDecimal costoLogo;
    java.math.BigDecimal costoOrdenTrabajo;
    String tipoItem;
    java.util.Map<String, String> technicalSpecs;

    public ItemEVN(Long productoId, Long proveedorId, Integer cantidad, Money precioUnitario, Money costoUnitario,
            java.math.BigDecimal costoProducto,
            java.math.BigDecimal costoLogo, java.math.BigDecimal costoOrdenTrabajo,
            String tipoItem, java.util.Map<String, String> technicalSpecs) {
        if (cantidad == null || cantidad <= 0) {
            throw new EVNBusinessException("La cantidad debe ser mayor a cero");
        }
        if (precioUnitario == null || precioUnitario.getAmount().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new EVNBusinessException("El precio unitario no puede ser negativo");
        }
        this.productoId = productoId;
        this.proveedorId = proveedorId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.costoUnitario = costoUnitario;
        this.costoProducto = costoProducto != null ? costoProducto : java.math.BigDecimal.ZERO;
        this.costoLogo = costoLogo != null ? costoLogo : java.math.BigDecimal.ZERO;
        this.costoOrdenTrabajo = costoOrdenTrabajo != null ? costoOrdenTrabajo : java.math.BigDecimal.ZERO;
        this.tipoItem = tipoItem;
        this.technicalSpecs = technicalSpecs != null ? technicalSpecs : java.util.Collections.emptyMap();
    }

    public Money getTotal() {
        if (precioUnitario == null || cantidad == null)
            return new Money(java.math.BigDecimal.ZERO, "CLP");
        return new Money(precioUnitario.getAmount().multiply(new java.math.BigDecimal(cantidad)),
                precioUnitario.getCurrency());
    }

    public java.math.BigDecimal getMargenItem() {
        if (precioUnitario == null || costoUnitario == null || cantidad == null)
            return java.math.BigDecimal.ZERO;
        java.math.BigDecimal totalVenta = precioUnitario.getAmount().multiply(new java.math.BigDecimal(cantidad));
        java.math.BigDecimal totalCosto = costoUnitario.getAmount().multiply(new java.math.BigDecimal(cantidad));
        return totalVenta.subtract(totalCosto);
    }
}
