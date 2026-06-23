package backend.com.comercial.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CrearNVCommand {

    /** Ignorado por el backend: el número lo asigna NumeroDocumentoService. */
    private Long numero;

    @NotNull(message = "La evaluación de negocio es obligatoria")
    private Long evaluacionNegocioId;

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El vendedor es obligatorio")
    private Long vendedorId;

    private Boolean esKit;

    @Size(max = 500)
    private String detalleKit;

    @NotNull(message = "La fecha de entrega estimada es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEntregaEstimada;

    @Valid
    private List<ItemNVDTO> items;
}
