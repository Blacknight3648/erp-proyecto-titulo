package backend.com.produccion.domain.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RecepcionOS {

    private Long idRecepcion;
    private Long osId;
    private LocalDate fechaRecepcion;
    private Integer cantidadRecibida;
    private Integer cantidadConforme;
    private Integer cantidadDefectuosa;
    private String responsable;
    private String observaciones;

    public RecepcionOS(Long idRecepcion, Long osId, LocalDate fechaRecepcion,
            Integer cantidadRecibida, Integer cantidadConforme, Integer cantidadDefectuosa,
            String responsable, String observaciones) {
        this.idRecepcion = idRecepcion;
        this.osId = osId;
        this.fechaRecepcion = fechaRecepcion != null ? fechaRecepcion : LocalDate.now();
        this.cantidadRecibida = cantidadRecibida != null ? cantidadRecibida : 0;
        this.cantidadConforme = cantidadConforme != null ? cantidadConforme : 0;
        this.cantidadDefectuosa = cantidadDefectuosa != null ? cantidadDefectuosa : 0;
        this.responsable = responsable;
        this.observaciones = observaciones;
    }
}
