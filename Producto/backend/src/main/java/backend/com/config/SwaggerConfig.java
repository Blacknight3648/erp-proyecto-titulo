package backend.com.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI antuanSAOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Antuan SA Gestion API")
                        .description("API del Backend de GestiÃ³n desarrollado con Spring Boot para Antuan SA.")
                        .version("v1.0.0")
                        .license(new License().name("Propiedad de Antuan SA").url("https://antuan.cl")));
    }
}


