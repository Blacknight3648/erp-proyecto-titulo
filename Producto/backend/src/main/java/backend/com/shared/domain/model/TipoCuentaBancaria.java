package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoCuentaBancaria {

    private Integer tipoCuentaId;
    private String denominacionCuenta;

}
