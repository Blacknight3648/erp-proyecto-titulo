package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoAccesorio {

    private Integer idTipoAccesorio;
    private String codigo;
    private String nombre;
}
