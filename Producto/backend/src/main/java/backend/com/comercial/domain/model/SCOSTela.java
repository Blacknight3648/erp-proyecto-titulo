package backend.com.comercial.domain.model;

import backend.com.shared.valueobjects.Money;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SCOSTela {
    private Long idTela;
    private String tempId;
    private String aplicacion;
    private String descripcion;
    private Long proveedorId;
    private String proveedorReferencia;
    private String composicion;
    private String color;
    private BigDecimal peso;
    private BigDecimal consumo;
    private String unidadMedida;
    private Money precioUnitario;
    private Money costoTotal;

    public SCOSTela(Long idTela, String aplicacion, String descripcion, Long proveedorId, String proveedorReferencia,
            String composicion, String color, BigDecimal peso,
            BigDecimal consumo, String unidadMedida, Money precioUnitario, String tempId) {
        this.idTela = idTela;
        this.tempId = tempId;
        this.aplicacion = aplicacion;
        this.descripcion = descripcion;
        this.proveedorId = proveedorId;
        this.proveedorReferencia = proveedorReferencia;
        this.composicion = composicion;
        this.color = color;
        this.peso = peso != null ? peso : BigDecimal.ZERO;
        this.consumo = consumo != null ? consumo : BigDecimal.ZERO;
        this.unidadMedida = unidadMedida != null ? unidadMedida : "mts";
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
