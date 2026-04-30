package backend.com.produccion.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadoOT {
    PENDIENTE("Pendiente"),
    EN_PROCESO("En Proceso"),
    FINALIZADA("Finalizada");

    private final String descripcion;
}
