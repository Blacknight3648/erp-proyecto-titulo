package backend.com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Activa el listener de auditoría JPA en toda la aplicación.
 *
 * Esto hace que las entidades que extienden AuditableJpaEntity reciban
 * automáticamente los valores de @CreatedDate y @LastModifiedDate en cada
 * persistencia, sin código adicional en los UseCase.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
