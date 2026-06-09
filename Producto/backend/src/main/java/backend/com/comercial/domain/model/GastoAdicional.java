package backend.com.comercial.domain.model;

import backend.com.shared.valueobjects.Money;
import lombok.Getter;

@Getter
public class GastoAdicional {
    private Long GAid;
    private TipoGastoAdicional tipoGasto;
    private Money monto;
    private java.util.List<GastoAdicionalDetalle> detalles = new java.util.ArrayList<>();

    public enum TipoGastoAdicional {
        FLETE,
        GARANTIA_SERIEDAD,
        GARANTIA_CUMPLIMIENTO,
        CERTIFICACION,
        MODIFICACION_PRENDA,
        MUESTRAS_FISICAS,
        ENTREGA_PERSONALIZADA,
        PEGADO_CINTA,
        COMISION,
        OTROS
    }

    protected GastoAdicional() {
        // Constructor protegido para JPA
    }

    public GastoAdicional(TipoGastoAdicional tipoGasto, Money monto) {
        this(tipoGasto, monto, null);
    }

    public GastoAdicional(TipoGastoAdicional tipoGasto, Money monto, java.util.List<GastoAdicionalDetalle> detalles) {
        this.tipoGasto = tipoGasto;
        this.monto = monto;
        if (detalles != null) {
            this.detalles.addAll(detalles);
        }
    }

    public void addDetalle(GastoAdicionalDetalle detalle) {
        this.detalles.add(detalle);
    }

    public void setMonto(Money monto) {
        this.monto = monto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GastoAdicional that = (GastoAdicional) o;
        return GAid != null && GAid.equals(that.GAid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
