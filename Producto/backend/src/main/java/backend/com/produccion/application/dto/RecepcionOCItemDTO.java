package backend.com.produccion.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecepcionOCItemDTO {

    private Long idRecepcionItem;
    private Long recepcionId;

    @NotNull(message = "El ocItemId es obligatorio")
    private Long ocItemId;

    @NotNull(message = "La cantidadRecibida es obligatoria")
    @PositiveOrZero
    private BigDecimal cantidadRecibida;

    @PositiveOrZero
    private BigDecimal cantidadConforme;

    @PositiveOrZero
    private BigDecimal cantidadRechazada;

    private String motivoRechazo;
}
