package backend.com.produccion.application.dto;

import lombok.Data;

@Data
public class OTResponse {
    private Long idOT;
    private Long numeroOT;
    private Long notaVentaId;
    private Integer nroItem;
    private String tipoOT;
    private String estadoOT;
    private String observaciones;
}
