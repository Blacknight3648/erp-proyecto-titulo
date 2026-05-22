package backend.com.shared.application.dto;

import backend.com.shared.domain.model.Banco;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BancoResponse {

    private Integer bancoId;
    private String nombreBanco;
    private String codigoBanco;

    public static BancoResponse fromDomain(Banco banco) {
        if (banco == null) return null;
        return BancoResponse.builder()
                .bancoId(banco.getBancoId())
                .nombreBanco(banco.getNombreBanco())
                .codigoBanco(banco.getCodigoBanco())
                .build();
    }
}
