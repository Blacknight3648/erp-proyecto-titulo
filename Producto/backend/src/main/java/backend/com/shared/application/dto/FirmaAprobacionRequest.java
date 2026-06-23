package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Cuerpo de solicitud genérico para acciones que requieren firma
 * de un aprobador (adjudicar, aprobar, rechazar, cancelar...).
 *
 * El campo `aprobador` es obligatorio en cualquier acción que registre
 * historial. `observacion` y `motivo` son opcionales/obligatorios según
 * la acción específica — la validación de "motivo obligatorio en rechazo"
 * vive en el UseCase porque depende del contexto.
 */
@Data
public class FirmaAprobacionRequest {

    @NotBlank(message = "El aprobador es obligatorio para firmar la acción")
    @Size(max = 100)
    private String aprobador;

    /**
     * Rol del actor que firma. Opcional para acciones sin chequeo de rol (EVN);
     * obligatorio donde se valide el rol (p. ej. aprobar/rechazar costeo).
     */
    @Size(max = 100)
    private String rol;

    @Size(max = 1000)
    private String observacion;

    @Size(max = 1000)
    private String motivo;
}
