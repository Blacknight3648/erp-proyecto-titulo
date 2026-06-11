package backend.com.produccion.domain.enums;

public enum EstadoOS {
    EMITIDA,
    EN_PROCESO,
    RECEPCIONADA,
    CERRADA;

    public boolean puedeTransicionarA(EstadoOS destino) {
        if (destino == null) return false;
        return switch (this) {
            case EMITIDA      -> destino == EN_PROCESO;
            case EN_PROCESO   -> destino == EN_PROCESO || destino == RECEPCIONADA;
            case RECEPCIONADA -> destino == RECEPCIONADA || destino == CERRADA;
            case CERRADA      -> false;
        };
    }
}
