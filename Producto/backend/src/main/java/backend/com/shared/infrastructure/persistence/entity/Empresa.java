package backend.com.shared.infrastructure.persistence.entity;

import backend.com.shared.validations.run.ValidRun;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empresa")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Empresa extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empresaId;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String nombreEmpresa;

    @Column(length = 20, unique = true, nullable = false)
    @ValidRun
    private String rutEmpresa;

    @Embedded
    private Direccion direccion;
}
