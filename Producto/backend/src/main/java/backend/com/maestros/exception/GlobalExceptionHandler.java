package backend.com.maestros.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Manejador global de excepciones para toda la API REST del módulo Maestros.
 *
 * <p>Centraliza el tratamiento de errores y garantiza que todas las respuestas
 * de error sigan el mismo formato {@link ApiErrorResponse}, evitando exponer
 * detalles internos del servidor al cliente.</p>
 *
 * <p>Jerarquía de manejo:</p>
 * <ul>
 *   <li>{@code RecursoNoEncontradoException} → 404 Not Found</li>
 *   <li>{@code DuplicadoException}            → 409 Conflict</li>
 *   <li>{@code ReglaNegocioException}         → 422 Unprocessable Entity</li>
 *   <li>{@code MethodArgumentNotValidException} → 400 Bad Request (validación)</li>
 *   <li>{@code IllegalArgumentException}      → 400 Bad Request</li>
 *   <li>{@code EntityNotFoundException}       → 404 Not Found (JPA estándar)</li>
 *   <li>{@code Exception}                     → 500 Internal Server Error (fallback)</li>
 * </ul>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─────────────────────────────────────────────────────────────────────────
    // 404 – Recurso no encontrado
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Maneja {@link RecursoNoEncontradoException}: recurso inexistente en BD.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> handleRecursoNoEncontrado(
            RecursoNoEncontradoException ex, HttpServletRequest request) {

        log.warn("[404] Recurso no encontrado | ruta={} | mensaje={}",
                request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    /**
     * Maneja {@link EntityNotFoundException} de JPA (compatibilidad con capas de acceso a datos).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex, HttpServletRequest request) {

        log.warn("[404] EntityNotFoundException | ruta={} | mensaje={}",
                request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 409 – Conflicto / Duplicado
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Maneja {@link DuplicadoException}: intento de crear un recurso ya existente.
     */
    @ExceptionHandler(DuplicadoException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicado(
            DuplicadoException ex, HttpServletRequest request) {

        log.warn("[409] Conflicto de duplicado | campo={} | valor={} | ruta={}",
                ex.getCampo(), ex.getValor(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage(), request));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 422 – Regla de negocio violada
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Maneja {@link ReglaNegocioException}: operación inválida según reglas del dominio.
     */
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiErrorResponse> handleReglaNegocio(
            ReglaNegocioException ex, HttpServletRequest request) {

        log.warn("[422] Regla de negocio violada | ruta={} | mensaje={}",
                request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(buildError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 400 – Errores de validación y parámetros inválidos
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Maneja {@link MethodArgumentNotValidException}: falla de validación Bean Validation
     * en el body del request ({@code @Valid}). Devuelve la lista detallada de campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ApiErrorResponse.CampoError> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toCampoError)
                .toList();

        log.warn("[400] Validación fallida | ruta={} | campos={}",
                request.getRequestURI(), errores.size());

        ApiErrorResponse body = ApiErrorResponse.builder()
                .estado(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .mensaje("La solicitud contiene " + errores.size() + " error(es) de validación")
                .ruta(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .erroresValidacion(errores)
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Maneja {@link MethodArgumentTypeMismatchException}: tipo incorrecto en parámetros
     * de ruta o query (ej: se espera un Integer y llega un String).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTipoIncorrecto(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String mensaje = String.format(
                "El parámetro '%s' recibió el valor '%s' que no es del tipo esperado '%s'",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido"
        );

        log.warn("[400] Tipo de argumento incorrecto | ruta={} | mensaje={}", request.getRequestURI(), mensaje);

        return ResponseEntity
                .badRequest()
                .body(buildError(HttpStatus.BAD_REQUEST, mensaje, request));
    }

    /**
     * Maneja {@link IllegalArgumentException}: argumento inválido genérico.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {

        log.warn("[400] Argumento inválido | ruta={} | mensaje={}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 500 – Error interno inesperado (fallback)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Maneja cualquier excepción no capturada por los handlers anteriores.
     * Registra el stack trace completo y devuelve un mensaje genérico al cliente
     * para no exponer detalles internos.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(
            Exception ex, HttpServletRequest request) {

        log.error("[500] Error interno inesperado | ruta={}", request.getRequestURI(), ex);

        return ResponseEntity
                .internalServerError()
                .body(buildError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Ha ocurrido un error interno. Por favor, contacte al administrador del sistema.",
                        request
                ));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Métodos auxiliares privados
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Construye un {@link ApiErrorResponse} estándar a partir de un {@link HttpStatus} y mensaje.
     */
    private ApiErrorResponse buildError(HttpStatus status, String mensaje, HttpServletRequest request) {
        return ApiErrorResponse.builder()
                .estado(status.value())
                .error(status.getReasonPhrase())
                .mensaje(mensaje)
                .ruta(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Convierte un {@link FieldError} de Spring en un {@link ApiErrorResponse.CampoError} serializable.
     */
    private ApiErrorResponse.CampoError toCampoError(FieldError fe) {
        return ApiErrorResponse.CampoError.builder()
                .campo(fe.getField())
                .valorRechazado(fe.getRejectedValue())
                .motivo(fe.getDefaultMessage())
                .build();
    }
}
