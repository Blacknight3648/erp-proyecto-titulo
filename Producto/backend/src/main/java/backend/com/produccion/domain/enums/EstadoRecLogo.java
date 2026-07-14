package backend.com.produccion.domain.enums;

public enum EstadoRecLogo {
    RECEPCION_COMPLETA("Recepción completa"),
    RECEPCION_PARCIAL("Recepción parcial"),
    NA("N/A");

    private final String descripcion;

    EstadoRecLogo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
