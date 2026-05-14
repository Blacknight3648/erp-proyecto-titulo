package backend.com.shared.application.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiglaDTO {

    private Long siglaId;

    @Size(max = 255, message = "La descripción de la sigla no puede exceder 255 caracteres")
    private String descripcionSigla;

    @Size(max = 100, message = "La abreviatura de la sigla no puede exceder 100 caracteres")
    private String siglaAbreviatura;
}
