package backend.com.shared.application.dto;

import backend.com.shared.domain.model.TipoDireccion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TipoDireccionResponse {

    private Integer tipoDireccionId;
    private String descripcion;

    public static TipoDireccionResponse fromDomain(TipoDireccion tipoDireccion) {
        if (tipoDireccion == null) return null;
        return TipoDireccionResponse.builder()
                .tipoDireccionId(tipoDireccion.getTipoDireccionId())
                .descripcion(tipoDireccion.getDescripcion())
                .build();
    }
}
