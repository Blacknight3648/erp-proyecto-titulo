package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banco {

    private Integer bancoId;
    private String nombreBanco;
    private String codigoBanco;
}
