package backend.com.comercial.infrastructure.persistence.entity;

import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "modelo_plantilla",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_modelo_articulo",
        columnNames = {"id_articulo"}
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

    /** Nombres de campo de la plantilla del artículo, en CSV. Ej: "forro,cuello,mangas". */
    @Column(name = "campos", nullable = false, columnDefinition = "TEXT")
    private String campos;
}
