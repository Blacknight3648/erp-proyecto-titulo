package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategoria {

    private Integer idSubCategoria;
    private String codigoSubcategoria;
    private String nombreSubCategoria;
    private Categoria categoria;
}