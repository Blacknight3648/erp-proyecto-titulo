package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Modelo {

    private Integer idModelo;
    private String codigoModelo;
    private String nombreModelo;
}
