package backend.com.produccion.application.dto;

import lombok.Data;

@Data
public class RegistrarAvanceCommand {
    private Integer cantidadProducida;
    private Integer cantidadMerma;
    private String motivoMerma;
    private String usuario;
    private String observacion;
}
