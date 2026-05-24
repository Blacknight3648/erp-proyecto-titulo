package backend.com.produccion.application.dto;

import backend.com.produccion.domain.model.EstadoHC;
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
public class HojaCompraDTO {

    private Long idHC;
    private String numeroHC;
    private Long opId;
    private Long costeoVersionId;
    private EstadoHC estado;
    private LocalDate fechaGeneracion;
    private String observaciones;
    private List<HojaCompraItemDTO> items;
}
