package backend.com.shared.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class HistorialEstado {
    private Long id;
    private String tipoEntidad;
    private Long entidadId;
    private String estadoAnterior;
    private String estadoNuevo;
    private String usuario;
    private String observacion;
    private LocalDateTime fecha;

    public HistorialEstado(Long id, String tipoEntidad, Long entidadId, String estadoAnterior,
            String estadoNuevo, String usuario, String observacion, LocalDateTime fecha) {
        this.id = id;
        this.tipoEntidad = tipoEntidad;
        this.entidadId = entidadId;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.usuario = usuario;
        this.observacion = observacion;
        this.fecha = fecha != null ? fecha : LocalDateTime.now();
    }

    public static HistorialEstado registrar(String tipoEntidad, Long entidadId,
            String estadoAnterior, String estadoNuevo, String usuario, String observacion) {
        return new HistorialEstado(null, tipoEntidad, entidadId, estadoAnterior, estadoNuevo,
                usuario, observacion, LocalDateTime.now());
    }
}
