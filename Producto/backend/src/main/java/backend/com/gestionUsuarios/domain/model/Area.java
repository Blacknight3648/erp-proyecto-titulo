package backend.com.gestionUsuarios.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Area {

    @EqualsAndHashCode.Include
    private Long areaId;
    private String nombre;
    private String descripcion;
}
