package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cinta_reflectante")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CintaReflectante extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cintaReflectanteId;

    @Column(length = 100, nullable = false)
    private String tipoCinta;

    @Column(length = 50, nullable = false)
    private String anchoCinta;
}
