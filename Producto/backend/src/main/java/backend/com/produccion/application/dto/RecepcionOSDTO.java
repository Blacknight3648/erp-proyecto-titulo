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
public class RecepcionOSDTO {

    private Long idRecepcion;
    private Long osId;
    private LocalDate fechaRecepcion;
    private Integer cantidadRecibida;
    private Integer cantidadConforme;
    private Integer cantidadDefectuosa;
    private String responsable;
    private String observaciones;
}
