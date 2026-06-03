package backend.com.maestros.dto.response;

import lombok.*;

/** Respuesta pública de AtributoTecnico. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AtributoTecnicoResponseDTO {

    private Integer idAtributo;
    private String codigoAtributo;
    private String clasificacion;
    private String descripcionTecnica;
    private String impactoErp;
}
