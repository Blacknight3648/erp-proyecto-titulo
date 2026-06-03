package backend.com.maestros.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Estructura estándar de respuesta de error para toda la API.
 * Todos los campos opcionales se omiten del JSON cuando son nulos.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    /** Código HTTP de la respuesta (ej: 404, 409, 422). */
    private final int estado;

    /** Nombre del estado HTTP (ej: "Not Found", "Conflict"). */
    private final String error;

    /** Mensaje descriptivo del error, en español. */
    private final String mensaje;

    /** Ruta del recurso que generó el error. */
    private final String ruta;

    /** Timestamp del error en formato ISO-8601. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    /**
     * Lista de errores de validación (solo presente en errores 400).
     * Cada entrada describe el campo inválido y el motivo.
     */
    private final List<CampoError> erroresValidacion;

    /** Representa un error de validación específico de un campo del request. */
    @Getter
    @Builder
    public static class CampoError {
        private final String campo;
        private final Object valorRechazado;
        private final String motivo;
    }
}
