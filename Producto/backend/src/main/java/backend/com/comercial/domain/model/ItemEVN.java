package backend.com.comercial.domain.model;

import backend.com.comercial.domain.enums.TipoItem;
import backend.com.shared.exception.EVNBusinessException;
import backend.com.shared.valueobjects.Money;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Value
public class ItemEVN {
    Integer articuloId;
    Long proveedorId;
    Integer nroItem;

    // Descriptores conocidos — atributos de primera clase (3FN)
    String descripcion;
    String modelo;
    String tela;
    String composicion;
    String genero;
    String codigoInterno;
    String codigoProveedor;
    String proveedorNombre;
    String color;

    Integer cantidad;
    Money precioUnitario;
    Money costoUnitario;

    BigDecimal costoProducto;
    BigDecimal costoLogo;
    BigDecimal costoOrdenTrabajo;

    TipoItem tipoItem;

    // Especificaciones verdaderamente dinámicas (no conocidas de antemano)
    List<ItemEspecificacion> technicalSpecs;

    // Vínculo con costeo/SCOS — solo aplica cuando tipoItem = "OP"
    Long costeoId;
    Long solicitudCostosId;

    public ItemEVN(Integer articuloId, Long proveedorId, Integer nroItem,
            String descripcion, String modelo, String tela, String composicion,
            String genero, String codigoInterno, String codigoProveedor, String proveedorNombre,
            String color,
            Integer cantidad, Money precioUnitario, Money costoUnitario,
            BigDecimal costoProducto, BigDecimal costoLogo, BigDecimal costoOrdenTrabajo,
            TipoItem tipoItem, List<ItemEspecificacion> technicalSpecs,
            Long costeoId, Long solicitudCostosId) {
        if (cantidad == null || cantidad <= 0) {
            throw new EVNBusinessException("La cantidad debe ser mayor a cero");
        }
        if (precioUnitario == null || precioUnitario.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new EVNBusinessException("El precio unitario no puede ser negativo");
        }
        this.articuloId = articuloId;
        this.proveedorId = proveedorId;
        this.nroItem = nroItem;
        this.descripcion = descripcion;
        this.modelo = modelo;
        this.tela = tela;
        this.composicion = composicion;
        this.genero = genero;
        this.codigoInterno = codigoInterno;
        this.codigoProveedor = codigoProveedor;
        this.proveedorNombre = proveedorNombre;
        this.color = color;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.costoUnitario = costoUnitario;
        this.costoProducto = costoProducto != null ? costoProducto : BigDecimal.ZERO;
        this.costoLogo = costoLogo != null ? costoLogo : BigDecimal.ZERO;
        this.costoOrdenTrabajo = costoOrdenTrabajo != null ? costoOrdenTrabajo : BigDecimal.ZERO;
        this.tipoItem = tipoItem;
        this.technicalSpecs = technicalSpecs != null ? technicalSpecs : Collections.emptyList();
        this.costeoId = costeoId;
        this.solicitudCostosId = solicitudCostosId;
    }

    public Money getTotal() {
        if (precioUnitario == null || cantidad == null)
            return new Money(BigDecimal.ZERO, "CLP");
        return new Money(precioUnitario.getAmount().multiply(new BigDecimal(cantidad)),
                precioUnitario.getCurrency());
    }

    public BigDecimal getMargenItem() {
        if (precioUnitario == null || costoUnitario == null || cantidad == null)
            return BigDecimal.ZERO;
        BigDecimal totalVenta = precioUnitario.getAmount().multiply(new BigDecimal(cantidad));
        BigDecimal totalCosto = costoUnitario.getAmount().multiply(new BigDecimal(cantidad));
        return totalVenta.subtract(totalCosto);
    }

    public BigDecimal getCostoTotalUnitario() {
        return costoProducto.add(costoLogo).add(costoOrdenTrabajo);
    }
}
