package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scos_plantilla")
@Getter
@Setter
public class SCOSPlantillaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_costos_id", nullable = true)
    private SolicitudCostosJpaEntity solicitudCostos;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String nombrePrenda;

    private String forro;
    private String relleno;
    private String gorro;
    private String cuello;

    @Column(name = "abotonadura_cierre")
    private String abotonaduraCierre;

    @Column(name = "cortes_aplicaciones")
    private String cortesAplicaciones;

    private String fuelles;
    private String mangas;

    @Column(name = "pretinas_ruedo")
    private String pretinasRuedo;

    private String bolsillos;

    @Column(name = "cinta_detalle")
    private String cintaDetalle;

    @Column(name = "logo_detalle")
    private String logoDetalle;

    @Column(name = "color_forro")
    private String colorForro;

    @Column(name = "accesorios_detalle")
    private String accesoriosDetalle;

    @Column(name = "obs_modelo", columnDefinition = "TEXT")
    private String obsModelo;

    @Column(name = "custom_fields", columnDefinition = "TEXT")
    private String customFields;

    private String genero;

    @ElementCollection
    @CollectionTable(name = "scos_plantilla_campos_activos", joinColumns = @JoinColumn(name = "plantilla_id"))
    @Column(name = "campo_key")
    private List<String> camposActivos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "scos_plantilla_telas", joinColumns = @JoinColumn(name = "plantilla_id"))
    private List<PlantillaTela> plantillaTelas = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "scos_plantilla_accesorios", joinColumns = @JoinColumn(name = "plantilla_id"))
    private List<PlantillaAccesorio> plantillaAccesorios = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "scos_plantilla_logotipos", joinColumns = @JoinColumn(name = "plantilla_id"))
    private List<PlantillaLogotipo> plantillaLogotipos = new ArrayList<>();

    // Mano de Obra
    @Column(name = "mo_prenda", precision = 12, scale = 2)
    private java.math.BigDecimal moPrenda;

    @Column(name = "mo_costura_sellada", precision = 12, scale = 2)
    private java.math.BigDecimal moCosturaSellada;

    @Column(name = "mo_acolchado", precision = 12, scale = 2)
    private java.math.BigDecimal moAcolchado;
    @OneToMany(mappedBy = "plantilla", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SCOSPlantillaMaterialVinculoJpaEntity> vinculos = new ArrayList<>();

    public void addVinculo(SCOSPlantillaMaterialVinculoJpaEntity vinculo) {
        vinculos.add(vinculo);
        vinculo.setPlantilla(this);
    }
}
