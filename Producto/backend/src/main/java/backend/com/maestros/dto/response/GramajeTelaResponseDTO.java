package backend.com.maestros.dto.response;

import lombok.*;
import java.math.BigDecimal;

/** Respuesta pública de GramajeTela. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class GramajeTelaResponseDTO {

    private Integer idGramaje;
    private String codigoGramaje;
    private BigDecimal valorGramosM2;
    private String categoriaVestuario;
}
