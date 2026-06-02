package backend.com.shared.infrastructure.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración manual de Flyway.
 *
 * Por defecto Spring Boot ejecuta Flyway ANTES de inicializar JPA/Hibernate.
 * En este proyecto Hibernate (ddl-auto=update) es quien crea las tablas, así
 * que las migrations de Flyway (que asumen tablas existentes para agregar
 * constraints, índices, etc.) deben correr DESPUÉS.
 *
 * Para evitar la "Circular depends-on relationship" en Spring Boot 3, no
 * registramos Flyway como un @Bean, sino que usamos un ApplicationRunner
 * que se ejecuta programáticamente una vez que TODO el contexto de Spring
 * (incluyendo Hibernate) ha terminado de cargar.
 */
@Configuration
public class FlywayConfig {

    @Bean
    public org.springframework.boot.ApplicationRunner flywayRunner(DataSource dataSource) {
        return args -> {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .baselineVersion("0")
                    .load();

            flyway.migrate();
        };
    }
}
