package backend.com.shared.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Respuesta estándar de error de la API REST.
 *
 * <p>
 * Formato uniforme retornado por {@code GlobalExceptionHandler} ante cualquier
 * excepción capturada. Los campos {@code null} se omiten en la serialización JSON.
 * </p>
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardErrorResponse {

    private final int estado;
    private final String error;
    private final String mensaje;
    private final String ruta;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    private final List<CampoError> erroresValidacion;

    /**
     * Detalle de un error de validación a nivel de campo.
     */
    @Getter
    @Builder
    public static class CampoError {
        private final String campo;
        private final Object valorRechazado;
        private final String motivo;
    }
}
