package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "descripcion_plantilla")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DescripcionPlantillaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_descripcion_plantilla")
    private Long idDescripcionPlantilla;

    @Column(name = "id_scos", nullable = false)
    private Long idSCOS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plantilla", nullable = false)
    private CamposPlantillaJpaEntity plantilla;

    @Column(name = "valor_descripcion", length = 500)
    private String valorDescripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @OneToMany(
        mappedBy = "descripcionPlantilla",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<SCOSPlantillaMaterialVinculoJpaEntity> vinculos = new ArrayList<>();
}
