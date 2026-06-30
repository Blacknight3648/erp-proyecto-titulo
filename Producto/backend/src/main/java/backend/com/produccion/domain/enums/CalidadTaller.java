package backend.com.produccion.domain.enums;

public enum CalidadTaller {
    APROBADO("Aprobado"),
    RECHAZADO("Rechazado"),
    CON_OBSERVACIONES("Con Observaciones");

    private final String descripcion;

    CalidadTaller(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
