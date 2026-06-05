package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moneda {

    private Integer idMoneda;
    private String codigoMoneda;
    private String nombreMoneda;
    private String simbolo;
}