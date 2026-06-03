package backend.com.maestros.domain.entity.catalogo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "color_tela")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColorTela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_color")
    private Integer idColor;

    @Column(name = "codigo_color", nullable = false, unique = true, length = 10)
    private String codigoColor;

    @Column(name = "descripcion_color", nullable = false, length = 40)
    private String descripcionColor;

    @Column(name = "es_pantone", nullable = false)
    private Boolean esPantone = false;
}
