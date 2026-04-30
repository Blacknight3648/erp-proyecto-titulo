package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "scos_costo_fijo")
@Getter
@Setter
public class SCOSCostoFijoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSCOSCostoFijo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_costos_id", nullable = true)
    private SolicitudCostosJpaEntity solicitudCostos;

    @Column(precision = 12, scale = 2)
    private BigDecimal hilo;

    @Column(name = "mano_obra_simple", precision = 12, scale = 2)
    private BigDecimal manoObraSimple;

    @Column(name = "mano_obra_gratificacion", precision = 12, scale = 2)
    private BigDecimal manoObraGratificacion;

    @Column(precision = 12, scale = 2)
    private BigDecimal etiqueta;

    @Column(precision = 12, scale = 2)
    private BigDecimal embalaje;

    @Column(precision = 12, scale = 2)
    private BigDecimal flete;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    @Column(length = 3)
    private String monedaTotal;
}
