package backend.com.produccion.domain.enums;

public enum EstadoIdaLogo {
    IDA_COMPLETA("Ida completa"),
    IDA_PARCIAL("Ida parcial"),
    NA("N/A");

    private final String descripcion;

    EstadoIdaLogo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
