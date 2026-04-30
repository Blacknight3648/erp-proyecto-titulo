package backend.com.produccion.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadoOP {
    PENDIENTE("Pendiente de Inicio"),
    EN_PROCESO("En Proceso"),
    DETENIDA("Detenida"),
    COMPLETADA("Completada"),
    CANCELADA("Cancelada");

    private final String descripcion;
}
