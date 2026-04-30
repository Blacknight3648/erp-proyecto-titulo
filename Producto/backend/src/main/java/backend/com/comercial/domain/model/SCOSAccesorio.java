package backend.com.comercial.domain.model;

import backend.com.shared.valueobjects.Money;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SCOSAccesorio {
    private Long idAccesorio;
    private String tempId;
    private String tipo;
    private String descripcion;
    private Integer cantidad;
    private Long proveedorId;
    private String proveedorReferencia;
    private BigDecimal consumo;
    private String unidadMedida;
    private Money precioUnitario;
    private Money costoTotal;

    public SCOSAccesorio(Long idAccesorio, String tipo, String descripcion, Integer cantidad, Long proveedorId,
            String proveedorReferencia, BigDecimal consumo, String unidadMedida, Money precioUnitario, String tempId) {
        this.idAccesorio = idAccesorio;
        this.tempId = tempId;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.cantidad = cantidad != null ? cantidad : 0;
        this.proveedorId = proveedorId;
        this.proveedorReferencia = proveedorReferencia;
        this.consumo = consumo != null ? consumo : BigDecimal.ZERO;
        this.unidadMedida = unidadMedida != null ? unidadMedida : "un";
        this.precioUnitario = precioUnitario;
        calcularCostoTotal();
    }

    private void calcularCostoTotal() {
        if (this.precioUnitario != null && this.consumo != null) {
            this.costoTotal = this.precioUnitario.multiply(this.consumo);
        } else {
            this.costoTotal = new Money(BigDecimal.ZERO, "CLP");
        }
    }
}
