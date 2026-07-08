package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtributoAccesorioValor {

    private Integer idValor;
    private AtributoAccesorioDefinicion definicion;
    private String valorTexto;
    private ColorTela colorReferencia;
}
