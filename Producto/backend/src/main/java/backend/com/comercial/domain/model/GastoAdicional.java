package backend.com.comercial.domain.model;

import backend.com.shared.valueobjects.Money;
import lombok.Getter;

@Getter
public class GastoAdicional {
    private Long GAid;
    private TipoGastoAdicional tipoGasto;
    private Money monto;
    private String metadataJson;

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

    public GastoAdicional(TipoGastoAdicional tipoGasto, Money monto, String metadataJson) {
        this.tipoGasto = tipoGasto;
        this.monto = monto;
        this.metadataJson = metadataJson;
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
