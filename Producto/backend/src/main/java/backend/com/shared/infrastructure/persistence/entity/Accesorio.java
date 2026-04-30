package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accesorio")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Accesorio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accesorioId;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nombreAccesorio;

    @Column(length = 200)
    private String descripcionAccesorio;
}
