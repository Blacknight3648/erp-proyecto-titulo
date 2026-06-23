package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategoriaTela {

    private Integer idSubCategoriaTela;
    private String codigoSubCategoriaTela;
    private String nombreSubCategoriaTela;
    private CategoriaTela categoriaTela;
}
