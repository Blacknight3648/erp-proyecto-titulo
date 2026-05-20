package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "produccion_costeo_item_versiones")
@Getter
@Setter
public class CosteoItemVersionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCosteoItemVersion;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "costeo_version_id", nullable = false)
    private CosteoVersionJpaEntity costeoVersion;

    @Column(name = "costeo_item_id", nullable = false)
    private Long costeoItemId;

    @Column(name = "tipo_insumo", length = 30, nullable = false)
    private String tipoInsumo;

    @Column(name = "insumo_id")
    private Long insumoId;

    @Column(name = "nombre_insumo")
    private String nombreInsumo;

    @Column(precision = 10, scale = 4)
    private BigDecimal consumo;

    @Column(precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(precision = 12, scale = 2)
    private BigDecimal costoTotal;

    @Column(name = "costeo_item_version_id_padre")
    private Long costeoItemVersionIdPadre;

    private Boolean activo;

}
