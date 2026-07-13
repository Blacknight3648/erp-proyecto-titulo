package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modelo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeloJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Integer idModelo;

    @Column(name = "codigo_modelo", nullable = false, unique = true, length = 10)
    private String codigoModelo;

    @Column(name = "nombre_modelo", nullable = false, unique = true, length = 60)
    private String nombreModelo;
}
