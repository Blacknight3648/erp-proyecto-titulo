package backend.com.shared.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GramajeTelaDTO {

    private Integer idGramaje;

    @Size(max = 10, message = "El código no puede superar 10 caracteres")
    private String codigoGramaje;

    @NotNull(message = "El valor en gr/m² es obligatorio")
    @DecimalMin(value = "0.01", message = "El gramaje debe ser mayor a cero")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal valorGramosM2;

    @Size(max = 60)
    private String categoriaVestuario;
}
