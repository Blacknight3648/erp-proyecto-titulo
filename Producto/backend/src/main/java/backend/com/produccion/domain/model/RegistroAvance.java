package backend.com.produccion.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RegistroAvance {
    private Long id;
    private Long ordenTrabajoId;
    private Integer cantidadProducida;
    private Integer cantidadMerma;
    private String motivoMerma;
    private String usuario;
    private String observacion;
    private LocalDateTime fecha;

    public RegistroAvance(Long id, Long ordenTrabajoId, Integer cantidadProducida,
            Integer cantidadMerma, String motivoMerma, String usuario, String observacion,
            LocalDateTime fecha) {
        this.id = id;
        this.ordenTrabajoId = ordenTrabajoId;
        this.cantidadProducida = cantidadProducida != null ? cantidadProducida : 0;
        this.cantidadMerma = cantidadMerma != null ? cantidadMerma : 0;
        this.motivoMerma = motivoMerma;
        this.usuario = usuario;
        this.observacion = observacion;
        this.fecha = fecha != null ? fecha : LocalDateTime.now();
    }

    public static RegistroAvance crear(Long otId, Integer producida, Integer merma,
            String motivoMerma, String usuario, String observacion) {
        return new RegistroAvance(null, otId, producida, merma, motivoMerma, usuario, observacion,
                LocalDateTime.now());
    }
}
