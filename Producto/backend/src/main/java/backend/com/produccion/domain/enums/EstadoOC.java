package backend.com.produccion.domain.enums;

public enum EstadoOC {
    EMITIDA,
    ENVIADA,
    RECEPCIONADA_PARCIAL,
    RECEPCIONADA,
    CERRADA,
    RECHAZADA;

    public boolean puedeTransicionarA(EstadoOC destino) {
        if (destino == null) return false;
        return switch (this) {
            case EMITIDA              -> destino == ENVIADA || destino == RECHAZADA;
            case ENVIADA              -> destino == RECEPCIONADA_PARCIAL || destino == RECEPCIONADA;
            case RECEPCIONADA_PARCIAL -> destino == RECEPCIONADA_PARCIAL || destino == RECEPCIONADA;
            case RECEPCIONADA         -> destino == CERRADA;
            case CERRADA              -> false;
            case RECHAZADA            -> destino == EMITIDA;
        };
    }
}
