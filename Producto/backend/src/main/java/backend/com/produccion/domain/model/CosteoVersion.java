package backend.com.produccion.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import backend.com.shared.valueobjects.Money;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Getter
public class CosteoVersion {
    private Long idCosteoVersion;
    private Long costeoId;
    private Integer numeroVersion;
    private LocalDateTime fechaCreacion;
    private String motivoCambio;
    private String usuarioCreador;

    private List<CosteoItemVersion> items = new ArrayList<>();

    private Money totalManoObra;
    private String observacionesManoObra;
    private Money totalHilo;
    private Money totalFlete;
    private Money totalEmbalaje;
    private Money totalEtiquetas;
    private BigDecimal porcentajeCostoFijo;
    private Money costoTotalMateriaPrima;
    private BigDecimal margenBrutoSugerido;
    private Money precioVentaSugerido;

    public CosteoVersion(Long idCosteoVersion, Long costeoId, Integer numeroVersion, LocalDateTime fechaCreacion,
            String motivoCambio, String usuarioCreador, List<CosteoItemVersion> items, Money totalManoObra,
            String observacionesManoObra, Money totalHilo, Money totalFlete, Money totalEmbalaje, Money totalEtiquetas,
            BigDecimal porcentajeCostoFijo, Money costoTotalMateriaPrima, BigDecimal margenBrutoSugerido,
            Money precioVentaSugerido) {
        this.idCosteoVersion = idCosteoVersion;
        this.costeoId = costeoId;
        this.numeroVersion = numeroVersion;
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : LocalDateTime.now();
        this.motivoCambio = motivoCambio;
        this.usuarioCreador = usuarioCreador;
        this.items = items != null ? items : new ArrayList<>();
        this.totalManoObra = totalManoObra;
        this.observacionesManoObra = observacionesManoObra;
        this.totalHilo = totalHilo;
        this.totalFlete = totalFlete;
        this.totalEmbalaje = totalEmbalaje;
        this.totalEtiquetas = totalEtiquetas;
        this.porcentajeCostoFijo = porcentajeCostoFijo;
        this.costoTotalMateriaPrima = costoTotalMateriaPrima;
        this.margenBrutoSugerido = margenBrutoSugerido;
        this.precioVentaSugerido = precioVentaSugerido;
    }

    public List<CosteoItemVersion> getItems() {
        return Collections.unmodifiableList(items);
    }
}
