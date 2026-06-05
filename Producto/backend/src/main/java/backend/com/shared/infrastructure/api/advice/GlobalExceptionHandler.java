package backend.com.shared.infrastructure.api.advice;

import backend.com.shared.exception.BaseException;
import backend.com.shared.exception.StandardErrorResponse;
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
 * Manejador global de excepciones para toda la API REST.
 *
 * <p>
 * Centraliza el tratamiento de errores y garantiza que todas las respuestas
 * sigan el mismo formato {@link StandardErrorResponse}, sin exponer detalles
 * internos del servidor al cliente.
 * </p>
 *
 * <p>
 * Jerarquía de manejo:
 * </p>
 * <ul>
 * <li>{@link BaseException} → status definido por cada subclase
 * (EntityNotFoundException 404, BusinessRuleException 422,
 * DuplicadoException 409, ValidationException 400, …)</li>
 * <li>{@link EntityNotFoundException} (JPA) → 404 Not Found</li>
 * <li>{@link MethodArgumentNotValidException} → 400 Bad Request con detalle de
 * campos</li>
 * <li>{@link MethodArgumentTypeMismatchException} → 400 Bad Request</li>
 * <li>{@link IllegalArgumentException} → 400 Bad Request</li>
 * <li>{@link Exception} → 500 Internal Server Error (fallback)</li>
 * </ul>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─────────────────────────────────────────────────────────────────────────
    // BaseException — captura todas las excepciones del dominio
    // (EntityNotFound, BusinessRule, Duplicado, Validation, etc.)
    // ─────────────────────────────────────────────────────────────────────────

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<StandardErrorResponse> handleBaseException(
            BaseException ex, HttpServletRequest request) {

        log.warn("[{}] {} | code={} | ruta={}",
                ex.getStatus().value(), ex.getMessage(), ex.getErrorCode(), request.getRequestURI());

        return ResponseEntity
                .status(ex.getStatus())
                .body(buildError(ex.getStatus(), ex.getMessage(), request));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 404 – EntityNotFoundException de JPA (no es de nuestra jerarquía)
    // ─────────────────────────────────────────────────────────────────────────

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardErrorResponse> handleJpaEntityNotFound(
            EntityNotFoundException ex, HttpServletRequest request) {

        log.warn("[404] EntityNotFoundException (JPA) | ruta={} | mensaje={}",
                request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 400 – Errores de validación y parámetros inválidos
    // ─────────────────────────────────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorResponse> handleValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<StandardErrorResponse.CampoError> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toCampoError)
                .toList();

        log.warn("[400] Validación fallida | ruta={} | campos={}",
                request.getRequestURI(), errores.size());

        StandardErrorResponse body = StandardErrorResponse.builder()
                .estado(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .mensaje("La solicitud contiene " + errores.size() + " error(es) de validación")
                .ruta(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .erroresValidacion(errores)
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardErrorResponse> handleTipoIncorrecto(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String mensaje = String.format(
                "El parámetro '%s' recibió el valor '%s' que no es del tipo esperado '%s'",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido");

        log.warn("[400] Tipo de argumento incorrecto | ruta={} | mensaje={}",
                request.getRequestURI(), mensaje);

        return ResponseEntity
                .badRequest()
                .body(buildError(HttpStatus.BAD_REQUEST, mensaje, request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {

        log.warn("[400] Argumento inválido | ruta={} | mensaje={}",
                request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 500 – Fallback para cualquier excepción no controlada
    // ─────────────────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardErrorResponse> handleGeneral(
            Exception ex, HttpServletRequest request) {

        log.error("[500] Error interno inesperado | ruta={}", request.getRequestURI(), ex);

        return ResponseEntity
                .internalServerError()
                .body(buildError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Ha ocurrido un error interno. Por favor, contacte al administrador del sistema.",
                        request));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private StandardErrorResponse buildError(HttpStatus status, String mensaje, HttpServletRequest request) {
        return StandardErrorResponse.builder()
                .estado(status.value())
                .error(status.getReasonPhrase())
                .mensaje(mensaje)
                .ruta(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private StandardErrorResponse.CampoError toCampoError(FieldError fe) {
        return StandardErrorResponse.CampoError.builder()
                .campo(fe.getField())
                .valorRechazado(fe.getRejectedValue())
                .motivo(fe.getDefaultMessage())
                .build();
    }
}
