package backend.com.shared.application.dto;

import backend.com.shared.domain.model.DatoBancario;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatoBancarioResponse {

    private Integer datoBancarioId;
    private String numeroCuenta;
    private BancoResponse banco;
    private TipoCuentaBancariaResponse tipoCuentaBancaria;

    public static DatoBancarioResponse fromDomain(DatoBancario dato) {
        if (dato == null) return null;
        return DatoBancarioResponse.builder()
                .datoBancarioId(dato.getDatoBancarioId())
                .numeroCuenta(dato.getNumeroCuenta())
                .banco(BancoResponse.fromDomain(dato.getBanco()))
                .tipoCuentaBancaria(TipoCuentaBancariaResponse.fromDomain(dato.getTipoCuentaBancaria()))
                .build();
    }
}
