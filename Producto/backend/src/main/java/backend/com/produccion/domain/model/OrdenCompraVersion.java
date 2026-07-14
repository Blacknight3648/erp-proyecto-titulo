package backend.com.produccion.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Snapshot congelado de una OrdenCompra (cabecera + ítems) al momento de
 * rechazarse. Análogo a {@link CosteoVersion}: preserva cómo estaba la OC
 * justo antes de que el reingreso posterior pudiera sobrescribir proveedor,
 * cantidades o precios.
 */
@Getter
public class OrdenCompraVersion {
    private Long idOCVersion;
    private Long ocId;
    private Integer numeroVersion;
    private LocalDateTime fechaCreacion;
    private String motivoCambio;
    private String usuarioCreador;

    private List<OrdenCompraItemVersion> items = new ArrayList<>();

    private Long proveedorId;
    private LocalDate fechaEntregaEstimada;
    private String observaciones;
    private BigDecimal totalNeto;

    public OrdenCompraVersion(Long idOCVersion, Long ocId, Integer numeroVersion, LocalDateTime fechaCreacion,
            String motivoCambio, String usuarioCreador, List<OrdenCompraItemVersion> items,
            Long proveedorId, LocalDate fechaEntregaEstimada, String observaciones, BigDecimal totalNeto) {
        this.idOCVersion = idOCVersion;
        this.ocId = ocId;
        this.numeroVersion = numeroVersion;
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : LocalDateTime.now();
        this.motivoCambio = motivoCambio;
        this.usuarioCreador = usuarioCreador;
        this.items = items != null ? items : new ArrayList<>();
        this.proveedorId = proveedorId;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.observaciones = observaciones;
        this.totalNeto = totalNeto;
    }

    public List<OrdenCompraItemVersion> getItems() {
        return Collections.unmodifiableList(items);
    }
}
