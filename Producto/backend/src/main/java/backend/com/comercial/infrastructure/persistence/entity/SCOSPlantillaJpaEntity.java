package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private String genero;

    /**
     * Schema dinámico de la prenda. Todos los atributos técnicos (gorro, cuello,
     * forro, etc.) y los campos personalizados creados por el usuario viven aquí
     * como pares clave/valor. La lista de claves activas se controla mediante
     * {@link #camposActivos}.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "detalles_prenda")
    private Map<String, Object> detallesPrenda = new HashMap<>();

    /**
     * Catálogo de labels para los campos personalizados creados por el usuario
     * en runtime. clave = identificador (`cf_xxx`), valor = label legible.
     * Los valores de esos campos viven en {@link #detallesPrenda} bajo la misma
     * clave.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "campos_personalizados")
    private Map<String, String> camposPersonalizados = new HashMap<>();

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
