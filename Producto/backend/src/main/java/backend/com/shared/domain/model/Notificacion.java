package backend.com.shared.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Notificación de sistema disparada por un evento real de negocio (OC generada,
 * HC aprobada, recepción registrada, etc.) en cualquier módulo. Es un feed
 * global, no dirigido a un usuario en particular.
 */
@Getter
public class Notificacion {
    private Long id;
    private String tipo;       // OC | HC | OS | LOGO | MP ...
    private String mensaje;
    private String categoria;  // COMPRAS | PRODUCCION | BODEGA ...
    private String prioridad;  // high | medium | normal
    private boolean leida;
    private LocalDateTime fecha;

    public Notificacion(Long id, String tipo, String mensaje, String categoria, String prioridad,
            boolean leida, LocalDateTime fecha) {
        this.id = id;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.categoria = categoria;
        this.prioridad = prioridad != null ? prioridad : "normal";
        this.leida = leida;
        this.fecha = fecha != null ? fecha : LocalDateTime.now();
    }

    public static Notificacion crear(String tipo, String mensaje, String categoria, String prioridad) {
        return new Notificacion(null, tipo, mensaje, categoria, prioridad, false, LocalDateTime.now());
    }

    public void marcarLeida() {
        this.leida = true;
    }
}
