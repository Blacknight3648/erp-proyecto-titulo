package backend.com.maestros.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/** Request para AtributoTecnico. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AtributoTecnicoRequestDTO {

    @NotBlank(message = "El código de atributo es obligatorio")
    @Size(max = 10)
    private String codigoAtributo;

    /** Ej: "RESISTENCIA", "ACABADO", "HIGIENE". */
    @Size(max = 20)
    private String clasificacion;

    @NotBlank(message = "La descripción técnica es obligatoria")
    @Size(max = 60)
    private String descripcionTecnica;

    @Size(max = 100)
    private String impactoErp;
}
