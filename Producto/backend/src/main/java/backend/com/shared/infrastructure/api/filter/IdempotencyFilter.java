package backend.com.shared.infrastructure.api.filter;

import backend.com.shared.infrastructure.persistence.entity.IdempotencyTokenJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.IdempotencyTokenJpaRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

/**
 * Filtro de Idempotencia HTTP.
 *
 * Aplica a cualquier POST/PATCH/PUT que traiga el header `Idempotency-Key`.
 * - Si el token ya fue procesado: devuelve la respuesta cacheada (incluye header `Idempotency-Replay: true`).
 * - Si el token es nuevo: ejecuta la request, y si la respuesta es 2xx la cachea en la tabla `idempotency_token`.
 *
 * El cliente es responsable de generar un UUID por operación (no por request).
 * Doble-click, retries y race conditions producen exactamente un efecto.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class IdempotencyFilter extends OncePerRequestFilter {

    public static final String HEADER = "Idempotency-Key";
    public static final String REPLAY_HEADER = "Idempotency-Replay";

    private static final Set<String> METODOS_APLICABLES = Set.of("POST", "PATCH", "PUT");
    private static final int MIN_LEN = 16;
    private static final int MAX_LEN = 100;

    private final IdempotencyTokenJpaRepository repository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        String token = request.getHeader(HEADER);
        String method = request.getMethod();

        if (token == null || token.isBlank() || !METODOS_APLICABLES.contains(method)) {
            chain.doFilter(request, response);
            return;
        }

        if (token.length() < MIN_LEN || token.length() > MAX_LEN) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"INVALID_IDEMPOTENCY_KEY\",\"message\":\"Idempotency-Key debe tener entre "
                            + MIN_LEN + " y " + MAX_LEN + " caracteres\"}");
            return;
        }

        Optional<IdempotencyTokenJpaEntity> cached = repository.findById(token);
        if (cached.isPresent()) {
            IdempotencyTokenJpaEntity c = cached.get();
            log.info("Idempotency replay: token={} method={} path={}", token, method, request.getRequestURI());
            response.setStatus(c.getStatusCode());
            response.setContentType("application/json");
            response.setHeader(REPLAY_HEADER, "true");
            if (c.getResponseBody() != null) {
                response.getWriter().write(c.getResponseBody());
            }
            return;
        }

        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);
        chain.doFilter(request, wrapper);

        int status = wrapper.getStatus();
        if (status >= 200 && status < 300) {
            try {
                byte[] body = wrapper.getContentAsByteArray();
                IdempotencyTokenJpaEntity entity = new IdempotencyTokenJpaEntity();
                entity.setToken(token);
                entity.setMetodo(method);
                entity.setPath(request.getRequestURI());
                entity.setStatusCode(status);
                entity.setResponseBody(new String(body, StandardCharsets.UTF_8));
                entity.setCreatedAt(LocalDateTime.now());
                repository.save(entity);
            } catch (Exception e) {
                log.warn("No se pudo persistir el token de idempotencia {}: {}", token, e.getMessage());
            }
        }

        wrapper.copyBodyToResponse();
    }
}
