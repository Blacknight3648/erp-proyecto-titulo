package backend.com.shared.application.dto;

import backend.com.shared.domain.model.TipoCuentaBancaria;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TipoCuentaBancariaResponse {

    private Integer tipoCuentaId;
    private String denominacionCuenta;

    public static TipoCuentaBancariaResponse fromDomain(TipoCuentaBancaria tipo) {
        if (tipo == null) return null;
        return TipoCuentaBancariaResponse.builder()
                .tipoCuentaId(tipo.getTipoCuentaId())
                .denominacionCuenta(tipo.getDenominacionCuenta())
                .build();
    }
}
