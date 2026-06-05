package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    private Integer idCategoria;
    private String codigoCategoria;
    private String nombreCategoria;
}