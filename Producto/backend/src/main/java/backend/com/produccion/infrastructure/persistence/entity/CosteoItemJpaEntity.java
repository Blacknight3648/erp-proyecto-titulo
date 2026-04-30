package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "produccion_costeo_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CosteoItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCosteoItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "costeo_id", nullable = false)
    private CosteoJpaEntity costeo;

    @Column(name = "tipo_insumo", length = 30, nullable = false)
    private String tipoInsumo; // TELA, ACCESORIO, LOGOTIPO, CINTA

    @Column(name = "insumo_id")
    private Long insumoId;

    @Column(name = "nombre_insumo")
    private String nombreInsumo;

    @Column(precision = 10, scale = 4)
    private BigDecimal consumo;

    @Column(name = "precio_unitario", precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "costo_total", precision = 12, scale = 2)
    private BigDecimal costoTotal;
}
