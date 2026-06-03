package backend.com.maestros.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/** Request para GramajeTela (gr/m²). */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class GramajeTelaRequestDTO {

    @NotBlank(message = "El código de gramaje es obligatorio")
    @Size(max = 10)
    private String codigoGramaje;

    @NotNull(message = "El valor en gr/m² es obligatorio")
    @DecimalMin(value = "0.01", message = "El gramaje debe ser mayor a cero")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal valorGramosM2;

    @Size(max = 60)
    private String categoriaVestuario;
}
