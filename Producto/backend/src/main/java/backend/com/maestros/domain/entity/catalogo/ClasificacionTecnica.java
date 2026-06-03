package backend.com.maestros.domain.entity.catalogo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clasificacion_tecnica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClasificacionTecnica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clasificacion_tecnica")
    private Integer idClasificacionTecnica;

    @Column(name = "nombre_clasificacion", nullable = false, unique = true, length = 40)
    private String nombreClasificacion;
}
