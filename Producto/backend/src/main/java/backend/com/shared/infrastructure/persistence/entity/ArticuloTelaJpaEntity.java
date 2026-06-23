package backend.com.shared.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Detalle específico para artículos tipo TELA.
 * Relación @OneToOne con Articulo usando PK compartida (@MapsId).
 */
@Entity
@Table(name = "articulo_tela")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloTelaJpaEntity {

    @Id
    @Column(name = "id_articulo")
    private Integer id;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "id_articulo")
    private ArticuloJpaEntity articulo;

    // ── Catálogos textiles ───────────────────────────────────

    @ManyToOne
    @JoinColumn(name = "id_familia_tela")
    private FamiliaTelaJpaEntity familiaTela;

    @ManyToOne
    @JoinColumn(name = "id_clasificacion_tecnica")
    private ClasificacionTecnicaJpaEntity clasificacionTecnica;

    @ManyToOne
    @JoinColumn(name = "id_composicion")
    private ComposicionJpaEntity composicion;

    @ManyToOne
    @JoinColumn(name = "id_gramaje")
    private GramajeTelaJpaEntity gramaje;

    // ── Campos propios ──────────────────────────────────────

    @Column(name = "abreviaturas_historicas", length = 60)
    private String abreviaturasHistoricas;

    @Column(name = "uso_tipico", length = 60)
    private String usoTipico;

    @Column(name = "observacion_proveedor", length = 200)
    private String observacionProveedor;

    // ── Relaciones N:M ──────────────────────────────────────

    @ManyToMany
    @JoinTable(name = "tela_color", joinColumns = @JoinColumn(name = "id_articulo"), inverseJoinColumns = @JoinColumn(name = "id_color"))
    private List<ColorTelaJpaEntity> colores;

    @ManyToMany
    @JoinTable(name = "tela_atributo_tecnico", joinColumns = @JoinColumn(name = "id_articulo"), inverseJoinColumns = @JoinColumn(name = "id_atributo"))
    private List<AtributoTecnicoJpaEntity> atributosTecnicos;
}