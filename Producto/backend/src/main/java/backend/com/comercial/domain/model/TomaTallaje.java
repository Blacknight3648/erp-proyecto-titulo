package backend.com.comercial.domain.model;

import backend.com.shared.valueobjects.Money;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TomaTallaje {
    private Long TomaTallajeId;
    private String observaciones;
    private LocalDate fechaProgramada;

    // Inputs para cálculo de costo (3FN: almacenar los hechos, no el derivado)
    private Integer diasXRecinto;
    private Integer persXRecinto;
    private BigDecimal colaccionXPers;
    private BigDecimal asignacionXPers;
    private BigDecimal peajes;
    private BigDecimal bencinaXLt;
    private BigDecimal kmTotal;
    private BigDecimal rendKmLt;
    private Integer recintos;

    // Cuadrícula de tallaje por persona
    private List<TomaTallajeDetalle> detalles = new ArrayList<>();

    protected TomaTallaje() {
    }

    public TomaTallaje(String observaciones, LocalDate fechaProgramada,
            Integer diasXRecinto, Integer persXRecinto,
            BigDecimal colaccionXPers, BigDecimal asignacionXPers,
            BigDecimal peajes, BigDecimal bencinaXLt, BigDecimal kmTotal, BigDecimal rendKmLt,
            Integer recintos, List<TomaTallajeDetalle> detalles) {
        this.observaciones = observaciones;
        this.fechaProgramada = fechaProgramada;
        this.diasXRecinto = diasXRecinto;
        this.persXRecinto = persXRecinto;
        this.colaccionXPers = colaccionXPers;
        this.asignacionXPers = asignacionXPers;
        this.peajes = peajes;
        this.bencinaXLt = bencinaXLt;
        this.kmTotal = kmTotal;
        this.rendKmLt = rendKmLt;
        this.recintos = recintos;
        if (detalles != null) {
            this.detalles.addAll(detalles);
        }
    }

    public void addDetalle(TomaTallajeDetalle detalle) {
        this.detalles.add(detalle);
    }

    /** Costo de personal: días × personas × (colación + asignación) */
    public BigDecimal calcPersonal() {
        if (diasXRecinto == null || persXRecinto == null) return BigDecimal.ZERO;
        BigDecimal dias = BigDecimal.valueOf(diasXRecinto);
        BigDecimal pers = BigDecimal.valueOf(persXRecinto);
        BigDecimal col = colaccionXPers != null ? colaccionXPers : BigDecimal.ZERO;
        BigDecimal asig = asignacionXPers != null ? asignacionXPers : BigDecimal.ZERO;
        return dias.multiply(pers).multiply(col.add(asig));
    }

    /** Costo de movilización: (bencina/rendimiento × km) + peajes */
    public BigDecimal calcMovilizacion() {
        BigDecimal paj = peajes != null ? peajes : BigDecimal.ZERO;
        if (bencinaXLt == null || kmTotal == null
                || rendKmLt == null || rendKmLt.compareTo(BigDecimal.ZERO) == 0)
            return paj;
        BigDecimal combustible = bencinaXLt.multiply(kmTotal)
                .divide(rendKmLt, 0, RoundingMode.HALF_UP);
        return combustible.add(paj);
    }

    /** Costo total: (personal + movilización) × cantidad de recintos/viajes */
    public Money getCostoTotal() {
        int rec = recintos != null ? recintos : 1;
        BigDecimal total = calcPersonal().add(calcMovilizacion())
                .multiply(BigDecimal.valueOf(rec));
        return new Money(total, "CLP");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TomaTallaje that = (TomaTallaje) o;
        return TomaTallajeId != null && TomaTallajeId.equals(that.TomaTallajeId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
