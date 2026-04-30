package backend.com.gestionUsuarios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackages = "backend.com.gestionUsuarios")
public class UserGlobalExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {

                ApiError error = ApiError.builder()
                                .message(ex.getMessage())
                                .status(HttpStatus.NOT_FOUND.value())
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {

                String message = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .findFirst()
                                .map(error -> error.getDefaultMessage())
                                .orElse("Error de validación");

                ApiError error = ApiError.builder()
                                .message(message)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity.badRequest().body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleGeneral(Exception ex) {

                ApiError error = ApiError.builder()
                                .message("Error interno del servidor")
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

        @SuppressWarnings("null")
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ApiError> handleBusiness(BusinessException ex) {

                ApiError error = ApiError.builder()
                                .message(ex.getMessage())
                                .status(ex.getStatus().value())
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(ex.getStatus())
                                .body(error);
        }
}
