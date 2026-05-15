package backend.com.shared.infrastructure.api.advice;

import backend.com.shared.exception.BaseException;
import backend.com.shared.exception.StandardErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class SharedGlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<StandardErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        log.error("Handled BaseException: {} - Code: {}", ex.getMessage(), ex.getErrorCode());
        return createResponse(ex.getStatus(), ex.getErrorCode(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
            HttpServletRequest request) {
        log.warn("Invalid Argument: {}", ex.getMessage());
        return createResponse(HttpStatus.BAD_REQUEST, "INVALID_ARGUMENT", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Internal Server Error: ", ex);
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR",
                ex.getMessage() != null ? ex.getMessage() : "Unknown Error", request.getRequestURI());
    }

    private ResponseEntity<StandardErrorResponse> createResponse(HttpStatus status, String errorCode, String message,
            String path) {
        StandardErrorResponse response = StandardErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .errorCode(errorCode)
                .build();
        return new ResponseEntity<>(response, status);
    }
}
