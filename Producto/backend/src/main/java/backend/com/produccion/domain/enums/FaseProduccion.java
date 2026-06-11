package backend.com.produccion.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FaseProduccion {
    CORTE("Corte", 1),
    CONFECCION("Confección", 2),
    BORDADO("Bordado", 3),
    ESTAMPADO("Estampado", 4),
    TERMINACION("Terminación", 5);

    private final String descripcion;
    private final int orden;
}
