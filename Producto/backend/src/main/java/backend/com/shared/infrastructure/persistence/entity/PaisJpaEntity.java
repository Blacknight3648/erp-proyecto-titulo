package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pais")
@Data
@NoArgsConstructor
public class PaisJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pais_id")
    private Long paisId;

    @NotBlank(message = "El nombre del país no puede estar en blanco")
    @Column(name = "nombre_pais", unique = true, length = 100, nullable = false)
    private String nombrePais;

}
