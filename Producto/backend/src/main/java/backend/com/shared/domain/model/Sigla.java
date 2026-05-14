package backend.com.shared.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sigla {

    private Long siglaId;
    private String descripcionSigla;
    private String siglaAbreviatura;
}
