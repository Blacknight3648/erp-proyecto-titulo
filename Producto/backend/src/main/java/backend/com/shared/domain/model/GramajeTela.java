package backend.com.shared.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GramajeTela {

    private Integer idGramaje;
    private String codigoGramaje;
    private BigDecimal valorGramosM2;
    private String categoriaVestuario;
}