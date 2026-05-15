package backend.com.shared.infrastructure.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Guardia provisional para el endpoint de historial de estado.
 *
 * Mientras no exista Spring Security + JWT, este interceptor exige al cliente
 * declarar su identidad mediante el header `X-User`. No es seguridad real
 * (cualquiera puede mandar el header), pero deja documentado el contrato y
 * evita accesos anónimos accidentales.
 *
 * Cuando se incorpore Spring Security, este componente se elimina y se
 * reemplaza por una anotación `@PreAuthorize("hasRole('ADMIN')")` directamente
 * sobre el controller.
 */
@Component
public class HistorialAccessInterceptor implements HandlerInterceptor {

    public static final String USER_HEADER = "X-User";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String user = request.getHeader(USER_HEADER);
        if (user == null || user.isBlank()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            try {
                response.getWriter().write(
                        "{\"error\":\"UNAUTHORIZED\",\"message\":\"Falta el header X-User para consultar el historial\"}");
            } catch (java.io.IOException ignored) {
            }
            return false;
        }
        return true;
    }
}
