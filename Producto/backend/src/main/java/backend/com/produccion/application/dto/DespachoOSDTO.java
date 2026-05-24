package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DespachoOSDTO {

    private Long idDespacho;
    private Long osId;
    private LocalDate fechaDespacho;
    private Integer cantidadDespachada;
    private String descripcion;
    private String responsable;
    private String observaciones;
}
