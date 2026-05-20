package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produccion_costeo_versiones", uniqueConstraints = @UniqueConstraint(columnNames = {
        "costeo_id", "numero_version" }))
@Getter
@Setter
public class CosteoVersionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCosteoVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "costeo_id", nullable = false)
    private CosteoJpaEntity costeo;

    @Column(name = "numero_version", nullable = false)
    private Integer numeroVersion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "motivo_cambio", nullable = true)
    private String motivoCambio;

    @Column(name = "usuario_creador", nullable = false)
    private String usuarioCreador;

    @OneToMany(mappedBy = "costeoVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<CosteoItemVersionJpaEntity> items = new java.util.ArrayList<>();

    @Column(name = "total_mano_obra", precision = 12, scale = 2)
    private BigDecimal totalManoObra;

    @Column(name = "total_hilo", precision = 12, scale = 2)
    private BigDecimal totalHilo;

    @Column(name = "total_flete", precision = 12, scale = 2)
    private BigDecimal totalFlete;

    @Column(name = "total_embalaje", precision = 12, scale = 2)
    private BigDecimal totalEmbalaje;

    @Column(name = "total_etiquetas", precision = 12, scale = 2)
    private BigDecimal totalEtiquetas;

    @Column(name = "porcentaje_costo_fijo", precision = 12, scale = 2)
    private BigDecimal porcentajeCostoFijo;

    @Column(name = "costo_total_materia_prima", precision = 12, scale = 2)
    private BigDecimal costoTotalMateriaPrima;

    @Column(name = "margen_bruto_sugerido", precision = 12, scale = 2)
    private BigDecimal margenBrutoSugerido;

    @Column(name = "precio_venta_sugerido", precision = 12, scale = 2)
    private BigDecimal precioVentaSugerido;

}
