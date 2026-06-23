package backend.com.produccion.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Ciclo de vida del Costeo.
 *
 * <pre>
 * BORRADOR  → COSTEADO, RECHAZADO
 * COSTEADO  → APROBADO, RECHAZADO, BORRADOR (reproceso)
 * APROBADO  → (terminal)
 * RECHAZADO → BORRADOR (reproceso)
 * </pre>
 *
 * - BORRADOR: estado inicial (pre-costeo creado al emitir la SCOS).
 * - COSTEADO: producción incorporó/confirmó los costos.
 * - RECHAZADO: hubo diferencias; exige motivo y versiona el costeo rechazado.
 * - APROBADO: habilita vincular el costeo a ítems de EVN/NV.
 */
@Getter
@RequiredArgsConstructor
public enum EstadoCosteo {
    BORRADOR("Borrador"),
    COSTEADO("Costeado"),
    APROBADO("Aprobado"),
    RECHAZADO("Rechazado");

    private final String descripcion;

    /** Indica si desde este estado es válido transicionar al estado destino. */
    public boolean puedeTransicionarA(EstadoCosteo destino) {
        if (destino == null) return false;
        return switch (this) {
            case BORRADOR  -> destino == COSTEADO || destino == RECHAZADO;
            case COSTEADO  -> destino == APROBADO || destino == RECHAZADO || destino == BORRADOR;
            case APROBADO  -> false;
            case RECHAZADO -> destino == BORRADOR;
        };
    }
}
