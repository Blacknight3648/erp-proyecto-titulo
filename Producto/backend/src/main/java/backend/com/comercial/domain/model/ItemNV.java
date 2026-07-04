package backend.com.comercial.domain.model;

import backend.com.comercial.domain.enums.TipoItem;
import backend.com.shared.valueobjects.Money;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ItemNV {
    private Long idItemNV;
    private Integer nroItem;
    private Integer articuloId;
    private String nombreProducto;
    private String modelo;
    private String tela;
    private String composicion;
    private String color;
    private String talla;
    private String genero;
    private String codigo;
    private Long proveedorId;
    private String nombreProveedor;
    private String llevaLogo;
    private TipoItem tipoItem;
    private Boolean requiereOt;
    private String detalleOt;
    private String logoDetalle;
    private Integer cantidad;
    private Money precioUnitario;
    private Money total;

    private Long opId;

    /**
     * Número de OP reservado al guardar el borrador. Se consume del contador atómico para
     * que nadie más pueda usarlo. Cuando la NV se emite, CrearOrdenProduccionUseCase usa
     * este número en vez de generar uno nuevo.
     */
    private String numeroOPReservado;

    /**
     * Costeo elegido manualmente por el usuario para este ítem, cuando es un ítem tipo OP
     * nuevo sin costeo heredado de la EVN. Se persiste para que la selección sobreviva
     * entre sesiones de edición del borrador. Al emitir la NV, CrearOrdenProduccionUseCase
     * usa este ID para vincular el costeo a la OP creada.
     */
    private Long costeoIdManual;

    private List<ItemNVTalla> tallas = new ArrayList<>();

    /** Vincula el id de la OP generada a partir de este ítem. Solo se puede asignar una vez. */
    public void vincularOp(Long opId) {
        if (this.opId != null && !this.opId.equals(opId)) {
            throw new IllegalStateException("El ítem NV ya está vinculado a la OP " + this.opId);
        }
        this.opId = opId;
    }

    public void setCosteoIdManual(Long costeoIdManual) {
        this.costeoIdManual = costeoIdManual;
    }

    public void setNumeroOPReservado(String numeroOPReservado) {
        this.numeroOPReservado = numeroOPReservado;
    }

    public ItemNV(Long idItemNV, Integer nroItem, Integer articuloId, String nombreProducto, String modelo, String tela, String composicion,
            String color, String talla, String genero, String codigo, Long proveedorId, String nombreProveedor,
            String llevaLogo, TipoItem tipoItem, Boolean requiereOt, String detalleOt,
            String logoDetalle, Integer cantidad, Money precioUnitario, List<ItemNVTalla> tallas) {
        this.idItemNV = idItemNV;
        this.nroItem = nroItem;
        this.articuloId = articuloId;
        this.nombreProducto = nombreProducto;
        this.modelo = modelo;
        this.tela = tela;
        this.composicion = composicion;
        this.color = color;
        this.talla = talla;
        this.genero = genero;
        this.codigo = codigo;
        this.proveedorId = proveedorId;
        this.nombreProveedor = nombreProveedor;
        this.llevaLogo = llevaLogo != null ? llevaLogo : "N/A";
        this.tipoItem = tipoItem;
        this.requiereOt = requiereOt != null ? requiereOt : false;
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
