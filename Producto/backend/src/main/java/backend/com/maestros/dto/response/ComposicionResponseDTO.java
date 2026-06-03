package backend.com.maestros.dto.response;

import lombok.*;

/** Respuesta pública de Composicion. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ComposicionResponseDTO {

    private Integer idComposicion;
    private String codigoComposicion;
    private String descripcionComposicion;
    private String clasificacion;
    private String usoTipico;
}
