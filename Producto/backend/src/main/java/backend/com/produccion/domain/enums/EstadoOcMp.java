package backend.com.produccion.domain.enums;

public enum EstadoOcMp {
    SIN_TELA_EN_MERCADO("sin tela en mercado"),
    OC_EMITIDA("OC emitida"),
    TELA_EN_STOCK("tela en stock"),
    EN_STOCK_Y_OC_EMITIDA("en stock y OC emitida"),
    NA("N/A");

    private final String descripcion;

    EstadoOcMp(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
