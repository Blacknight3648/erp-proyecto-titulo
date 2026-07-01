package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import jakarta.persistence.Column;

@Entity
@Table(name = "scos_logotipos")
@Getter
@Setter
public class SCOSLogotipoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_scos_logotipo")
    private Long idSCOSLogotipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_costos_id", nullable = true)
    private SolicitudCostosJpaEntity solicitudCostos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_cotizacion_id", nullable = true)
    private SolicitudCotizacionJpaEntity solicitudCotizacion;

    private String tipo;
    private String nombre;
    private String ubicacion;
    private String color;
    @Column(length = 50)
    private String tamano;
    private Integer cantidad;
    
    @Column(precision = 14, scale = 2)
    private BigDecimal precio;
}
