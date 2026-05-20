package backend.com.comercial.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadoSCOS {
    PENDIENTE("Pendiente"),
    APROBADA("Aprobada");

    private final String descripcion;
}
