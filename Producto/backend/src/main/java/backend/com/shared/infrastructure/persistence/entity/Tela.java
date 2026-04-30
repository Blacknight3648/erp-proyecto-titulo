package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tela")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Tela extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTela;

    @Column(nullable = false, length = 100)
    private String nombreTela;

    @Column(length = 200)
    private String composicion;

    @Column(length = 50)
    private String gramaje;
}
