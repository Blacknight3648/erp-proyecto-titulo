package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "produccion_costeos")
@Getter
@Setter
public class CosteoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCosteo;

    @Column(name = "solicitud_costos_id")
    private Long solicitudCostosId;

    /** Referencia suave a la NV (solo para costeos auto-creados sin solicitud de costos). */
    @Column(name = "nota_venta_id", unique = true)
    private Long notaVentaId;

    @OneToMany(mappedBy = "costeo", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<CosteoItemJpaEntity> items = new java.util.ArrayList<>();

    @Column(length = 20, nullable = false)
    private String numeroCosteo;

    @Column(name = "costo_hilos", precision = 12, scale = 2)
    private BigDecimal costoHilos;

    @Column(name = "costo_mano_obra", precision = 12, scale = 2)
    private BigDecimal costoManoObra;

    @Column(name = "costo_etiquetas", precision = 12, scale = 2)
    private BigDecimal costoEtiquetas;

    @Column(name = "costo_embalaje", precision = 12, scale = 2)
    private BigDecimal costoEmbalaje;

    @Column(name = "costo_flete", precision = 12, scale = 2)
    private BigDecimal costoFlete;

    @Column(name = "porcentaje_costo_fijo", precision = 12, scale = 2)
    private BigDecimal porcentajeCostoFijo;

    @Column(name = "precio_cinta_1", precision = 12, scale = 2)
    private BigDecimal precioCinta1;

    @Column(name = "cantidad_cinta_1", precision = 10, scale = 4)
    private BigDecimal cantidadCinta1;

    @Column(name = "precio_cinta_2", precision = 12, scale = 2)
    private BigDecimal precioCinta2;

    @Column(name = "cantidad_cinta_2", precision = 10, scale = 4)
    private BigDecimal cantidadCinta2;

    @Column(name = "vivo_reflectivo", precision = 12, scale = 2)
    private BigDecimal vivoReflectivo;

    @Column(name = "cantidad_vivo", precision = 10, scale = 4)
    private BigDecimal cantidadVivo;

    @Column(name = "costo_total_materia_prima", precision = 12, scale = 2)
    private BigDecimal costoTotalMateriaPrima;

    @Column(name = "margen_bruto_sugerido", precision = 12, scale = 2)
    private BigDecimal margenBrutoSugerido;

    @Column(name = "precio_venta_sugerido", precision = 12, scale = 2)
    private BigDecimal precioVentaSugerido;
}
