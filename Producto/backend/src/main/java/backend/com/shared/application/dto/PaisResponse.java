package backend.com.shared.application.dto;

import backend.com.shared.domain.model.Pais;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaisResponse {

    private Integer idPais;
    private String nombrePais;

    public static PaisResponse fromDomain(Pais pais) {
        if (pais == null) return null;
        return PaisResponse.builder()
                .idPais(pais.getIdPais())
                .nombrePais(pais.getNombrePais())
                .build();
    }
}
