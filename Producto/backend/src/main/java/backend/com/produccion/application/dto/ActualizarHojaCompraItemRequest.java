package backend.com.produccion.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarHojaCompraItemRequest {

    @NotNull(message = "La cantidad a comprar es obligatoria")
    private BigDecimal cantidadAComprar;

    private BigDecimal precioUnitarioRef;

    private BigDecimal cantidadStock;

    private String justificacionModificacion;
}
