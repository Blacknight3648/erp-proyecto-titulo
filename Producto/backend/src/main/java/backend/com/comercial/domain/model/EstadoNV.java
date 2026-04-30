package backend.com.comercial.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadoNV {
    BORRADOR("Borrador"),
    APROBADA("Aprobada"),
    EN_PRODUCCION("En ProducciÃ³n"),
    COMPLETADA("Completada"),
    ENTREGADA("Entregada"),
    CANCELADA("Cancelada");

    private final String descripcion;
}
