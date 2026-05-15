package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_token", indexes = {
        @Index(name = "idx_idem_created", columnList = "created_at")
})
@Getter
@Setter
public class IdempotencyTokenJpaEntity {

    @Id
    @Column(length = 100)
    private String token;

    @Column(length = 10, nullable = false)
    private String metodo;

    @Column(length = 255, nullable = false)
    private String path;

    @Column(name = "status_code", nullable = false)
    private Integer statusCode;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
