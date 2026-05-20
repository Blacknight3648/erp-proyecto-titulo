package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Clase base para entidades JPA que necesitan auditoría temporal automática.
 *
 * Al extenderla, la entidad gana las columnas `created_at` y `updated_at`
 * que Spring rellena automáticamente en cada save(), sin necesidad de tocar
 * el código de los UseCase o mappers.
 *
 * Requiere @EnableJpaAuditing en la configuración (ver JpaAuditingConfig).
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AuditableJpaEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
