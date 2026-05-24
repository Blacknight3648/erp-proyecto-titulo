package backend.com.produccion.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GenerarOCConsolidadaRequest {

    @NotNull(message = "El proveedorId es obligatorio")
    private Long proveedorId;

    @NotEmpty(message = "Debe enviar al menos un hcItemId para consolidar")
    private List<Long> hcItemIds;

    private LocalDate fechaEntregaEstimada;

    @Size(max = 500)
    private String observaciones;
}
