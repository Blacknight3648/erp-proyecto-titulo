package backend.com.shared.infrastructure.persistence.entity;

import backend.com.shared.validations.run.ValidRun;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empresa", indexes = {
                @Index(name = "idx_empresa_run", columnList = "run_empresa")
}, uniqueConstraints = {
                @UniqueConstraint(name = "uk_empresa_run", columnNames = "run_empresa")
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class EmpresaJpaEntity extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long empresaId;

        @ValidRun
        @Column(name = "run_empresa", nullable = false, length = 12)
        private String runEmpresa;

        @NotBlank
        @Size(max = 150)
        @Column(nullable = false, length = 150)
        private String razonSocial;

        @Size(max = 150)
        @Column(length = 150)
        private String nombreFantasia;

        @Size(max = 100)
        @Column(length = 100)
        private String giro;

        @Size(max = 200)
        @Column(length = 200)
        private String direccion;

        @Size(max = 100)
        @Column(length = 100)
        private String ciudad;

        @Size(max = 100)
        @Column(length = 100)
        private String region;

        @Size(max = 20)
        @Column(length = 20)
        private String telefono;

        @Size(max = 150)
        @Column(length = 150)
        private String correo;

        @Column(nullable = false)
        private boolean activo = true;
}