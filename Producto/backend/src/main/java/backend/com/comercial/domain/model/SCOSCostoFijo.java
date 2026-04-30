package backend.com.comercial.domain.model;

import backend.com.shared.valueobjects.Money;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SCOSCostoFijo {
    private Money hilo;
    private Money manoObraSimple;
    private Money manoObraGratificacion;
    private Money etiqueta;
    private Money embalaje;
    private Money flete;
    private Money total;

    public SCOSCostoFijo(Money hilo, Money manoObraSimple, Money manoObraGratificacion, 
                         Money etiqueta, Money embalaje, Money flete) {
        this.hilo = hilo != null ? hilo : new Money(BigDecimal.ZERO, "CLP");
        this.manoObraSimple = manoObraSimple != null ? manoObraSimple : new Money(BigDecimal.ZERO, "CLP");
        this.manoObraGratificacion = manoObraGratificacion != null ? manoObraGratificacion : new Money(BigDecimal.ZERO, "CLP");
        this.etiqueta = etiqueta != null ? etiqueta : new Money(BigDecimal.ZERO, "CLP");
        this.embalaje = embalaje != null ? embalaje : new Money(BigDecimal.ZERO, "CLP");
        this.flete = flete != null ? flete : new Money(BigDecimal.ZERO, "CLP");
        calcularTotal();
    }

    private void calcularTotal() {
        this.total = this.hilo.add(this.manoObraSimple)
                              .add(this.manoObraGratificacion)
                              .add(this.etiqueta)
                              .add(this.embalaje)
                              .add(this.flete);
    }
}
