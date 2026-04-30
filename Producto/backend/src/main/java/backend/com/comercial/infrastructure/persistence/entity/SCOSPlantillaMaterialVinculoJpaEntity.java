package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "scos_plantilla_material_vinculos")
@Getter
@Setter
public class SCOSPlantillaMaterialVinculoJpaEntity {
    @Transient
    private String tempMaterialId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantilla_id", nullable = false)
    private SCOSPlantillaJpaEntity plantilla;

    @Column(name = "field_name", nullable = false)
    private String fieldName; // e.g. "forro", "mangas"

    @Column(name = "material_type", nullable = false)
    private String materialType; // "TELA" or "ACCESORIO"

    @Column(name = "material_id")
    private Long materialId; // ID of SCOSTelaJpaEntity or SCOSAccesorioJpaEntity

    @Column(name = "cantidad")
    private Integer cantidad;
}
