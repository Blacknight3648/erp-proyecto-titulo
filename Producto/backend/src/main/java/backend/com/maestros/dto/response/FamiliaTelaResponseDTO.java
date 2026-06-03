package backend.com.maestros.dto.response;

import lombok.*;

/** Respuesta pública de FamiliaTela. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FamiliaTelaResponseDTO {

    private Integer idFamiliaTela;
    private String codigoFamilia;
    private String nombreFamilia;
}
