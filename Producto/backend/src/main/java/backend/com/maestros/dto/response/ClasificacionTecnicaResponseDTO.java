package backend.com.maestros.dto.response;

import lombok.*;

/** Respuesta pública de ClasificacionTecnica. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClasificacionTecnicaResponseDTO {

    private Integer idClasificacionTecnica;
    private String nombreClasificacion;
}
