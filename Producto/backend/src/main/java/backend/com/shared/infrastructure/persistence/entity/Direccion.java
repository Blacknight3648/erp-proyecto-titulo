package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Direccion {

    private String direccionId;

    @Column(nullable = false, length = 150)
    private String calle;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(length = 20)
    private String depto;

    @Column(nullable = false)
    private Long comunaId;

    @Column(nullable = false)
    private Long regionId;
}
