package backend.com.maestros.domain.entity.catalogo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "composicion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Composicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_composicion")
    private Integer idComposicion;

    @Column(name = "codigo_composicion", nullable = false, unique = true, length = 10)
    private String codigoComposicion;

    @Column(name = "descripcion_composicion", nullable = false, length = 60)
    private String descripcionComposicion;

    @Column(name = "clasificacion", length = 20)
    private String clasificacion;

    @Column(name = "uso_tipico", length = 60)
    private String usoTipico;
}
