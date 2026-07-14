package backend.com.produccion.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class GenerarOCLoteRequest {

    @NotEmpty(message = "Debe enviar al menos un grupo para consolidar")
    @Valid
    private List<GenerarOCConsolidadaRequest> grupos;
}
