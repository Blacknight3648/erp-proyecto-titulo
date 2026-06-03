package backend.com.maestros.dto.response;

import lombok.*;

/** Respuesta pública de ColorTela. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ColorTelaResponseDTO {

    private Integer idColor;
    private String codigoColor;
    private String descripcionColor;
    private Boolean esPantone;
}
