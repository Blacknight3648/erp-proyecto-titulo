package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plantilla")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamposPlantillaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plantilla")
    private Long idPlantilla;

    @Column(name = "nombre_campo", nullable = false, unique = true, length = 100)
    private String nombreCampo;
}
