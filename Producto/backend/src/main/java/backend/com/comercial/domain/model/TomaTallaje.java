package backend.com.comercial.domain.model;

import backend.com.shared.valueobjects.Money;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TomaTallaje {
    private Long TomaTallajeId;
    private Money costoTotal;
    private String observaciones;
    private LocalDate fechaProgramada;
    private String metadataJson;

    protected TomaTallaje() {
        // Para JPA o frameworks que requieran constructor vacío
    }

    public TomaTallaje(Money costoTotal, String observaciones, LocalDate fechaProgramada, String metadataJson) {
        this.costoTotal = costoTotal;
        this.observaciones = observaciones;
        this.fechaProgramada = fechaProgramada;
        this.metadataJson = metadataJson;
    }

    public void setCostoTotal(Money costoTotal) {
        this.costoTotal = costoTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TomaTallaje that = (TomaTallaje) o;
        return TomaTallajeId != null && TomaTallajeId.equals(that.TomaTallajeId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
