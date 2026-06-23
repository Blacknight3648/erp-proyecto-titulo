package backend.com.comercial.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadoSCOS {
    PENDIENTE("Pendiente"),
    APROBADA("Aprobada");

    private final String descripcion;
}
