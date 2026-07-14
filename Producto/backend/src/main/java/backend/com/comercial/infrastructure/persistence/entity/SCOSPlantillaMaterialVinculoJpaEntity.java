package backend.com.comercial.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scos_plantilla_material_vinculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SCOSPlantillaMaterialVinculoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_scos_plantilla_material_vinculo")
    private Long idSCOSPlantillaMaterialVinculo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_descripcion_plantilla", nullable = false)
    private DescripcionPlantillaJpaEntity descripcionPlantilla;

    @Column(name = "material_type", nullable = false, length = 20)
    private String materialType;   // "TELA" | "ACCESORIO"

    @Column(name = "material_id", nullable = false)
    private Long materialId;

    @Column(name = "cantidad")
    private Integer cantidad;
}
