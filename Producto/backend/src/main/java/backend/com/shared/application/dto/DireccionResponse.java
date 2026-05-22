package backend.com.shared.application.dto;

import backend.com.shared.domain.model.Direccion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DireccionResponse {

    private Long direccionId;
    private String calle;
    private String numero;
    private String depto;
    private TipoDireccionResponse tipoDireccion;
    private ComunaResponse comuna;

    public static DireccionResponse fromDomain(Direccion direccion) {
        if (direccion == null) return null;
        return DireccionResponse.builder()
                .direccionId(direccion.getDireccionId())
                .calle(direccion.getCalle())
                .numero(direccion.getNumero())
                .depto(direccion.getDepto())
                .tipoDireccion(TipoDireccionResponse.fromDomain(direccion.getTipoDireccion()))
                .comuna(ComunaResponse.fromDomain(direccion.getComuna()))
                .build();
    }
}
