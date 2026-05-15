package backend.com.config;

import backend.com.shared.infrastructure.api.interceptor.HistorialAccessInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final HistorialAccessInterceptor historialAccessInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(historialAccessInterceptor)
                .addPathPatterns("/api/v1/historial-estado/**")
                .addPathPatterns("/api/v1/comercial/evaluaciones-negocio/*/historial")
                .addPathPatterns("/api/v1/comercial/notas-venta/*/historial");
    }
}
