package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatoBancario {

    private Integer datoBancarioId;
    private String numeroCuenta;
    private Banco banco;
    private TipoCuentaBancaria tipoCuentaBancaria;
}
