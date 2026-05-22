package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comuna")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Comuna extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comuna_id")
    private Long comunaId;

    @NotBlank(message = "El nombre de la comuuna no puede estar en blanco")
    @Column(name = "nombre_comuna", length = 100, nullable = false)
    private String nombreComuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;
}
