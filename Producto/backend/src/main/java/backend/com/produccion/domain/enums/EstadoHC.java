package backend.com.produccion.domain.enums;

public enum EstadoHC {
    BORRADOR,
    APROBADA,
    CERRADA;

    public boolean puedeTransicionarA(EstadoHC destino) {
        if (destino == null) return false;
        return switch (this) {
            case BORRADOR -> destino == APROBADA;
            case APROBADA -> destino == CERRADA || destino == BORRADOR;
            case CERRADA  -> destino == BORRADOR || destino == APROBADA;
        };
    }
}
