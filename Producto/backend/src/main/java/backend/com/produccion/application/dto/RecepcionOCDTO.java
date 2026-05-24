package backend.com.produccion.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecepcionOCDTO {

    private Long idRecepcion;
    private Long ocId;
    private LocalDate fechaRecepcion;

    @Size(max = 50)
    private String numeroGuia;

    @Size(max = 100)
    private String responsable;

    private String observaciones;

    @Valid
    @NotEmpty(message = "Debe registrar al menos un item recibido")
    private List<RecepcionOCItemDTO> items;
}
