package backend.com.shared.infrastructure.persistence.entity;

import backend.com.shared.domain.enums.TipoArticulo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_articulo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoArticuloJpaEntity {

    @Id
    @Column(name = "id_tipo_articulo")
    private Integer idTipoArticulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private TipoArticulo codigo;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;
}
