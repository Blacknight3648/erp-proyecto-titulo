package backend.com.comercial.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadoNV {
    BORRADOR("Borrador"),
    EMITIDA("Emitida"),
    
    EN_PRODUCCION("En ProducciÃ³n"),
    COMPLETADA("Completada"),
    ENTREGADA("Entregada"),
    CANCELADA("Cancelada");

    private final String descripcion;
}
