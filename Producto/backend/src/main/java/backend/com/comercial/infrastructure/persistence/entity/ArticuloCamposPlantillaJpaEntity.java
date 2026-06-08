package backend.com.comercial.infrastructure.persistence.entity;

import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "modelo_plantilla",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_modelo_articulo_plantilla",
        columnNames = {"id_articulo", "id_plantilla"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloCamposPlantillaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo_plantilla")
    private Long idModeloPlantilla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_articulo", nullable = false)
    private ArticuloJpaEntity articulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plantilla", nullable = false)
    private CamposPlantillaJpaEntity plantilla;
}
