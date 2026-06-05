package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoArticulo {

    private Integer idTipoArticulo;
    private String codigo;
    private String nombre;
}
