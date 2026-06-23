package backend.com.produccion.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import backend.com.produccion.domain.enums.TipoServicioOS;

@Data
public class CrearOSRequest {

    @NotNull(message = "El opId es obligatorio")
    private Long opId;

    @NotNull(message = "El proveedorId es obligatorio")
    private Long proveedorId;

    @NotNull(message = "El tipoServicio es obligatorio")
    private TipoServicioOS tipoServicio;

    private LocalDate fechaEntregaEstimada;

    @Size(max = 1000)
    private String descripcionTrabajo;

    @NotNull(message = "La cantidad pactada es obligatoria")
    @Positive(message = "La cantidad pactada debe ser positiva")
    private Integer cantidadPactada;

    @PositiveOrZero(message = "El precio unitario debe ser no negativo")
    private BigDecimal precioUnitario;

    @Size(max = 500)
    private String observaciones;
}
