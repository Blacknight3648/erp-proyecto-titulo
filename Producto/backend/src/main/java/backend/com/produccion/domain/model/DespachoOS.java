package backend.com.produccion.domain.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DespachoOS {

    private Long idDespacho;
    private Long osId;
    private LocalDate fechaDespacho;
    private Integer cantidadDespachada;
    private String descripcion;
    private String responsable;
    private String observaciones;

    public DespachoOS(Long idDespacho, Long osId, LocalDate fechaDespacho, Integer cantidadDespachada,
            String descripcion, String responsable, String observaciones) {
        this.idDespacho = idDespacho;
        this.osId = osId;
        this.fechaDespacho = fechaDespacho != null ? fechaDespacho : LocalDate.now();
        this.cantidadDespachada = cantidadDespachada != null ? cantidadDespachada : 0;
        this.descripcion = descripcion;
        this.responsable = responsable;
        this.observaciones = observaciones;
    }
}
