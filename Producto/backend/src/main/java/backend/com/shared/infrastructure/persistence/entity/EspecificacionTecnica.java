package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "especificacion_tecnica")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class EspecificacionTecnica extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long especificacionTecnicaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private ProductoJpaEntity producto;

    @Column(columnDefinition = "TEXT")
    private String detallesEspecificacionTecnica;

    @Column(name = "url_documento", length = 300)
    private String urlDocumentoEspecificacionTecnica;
}
