package backend.com.maestros.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "moneda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moneda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_moneda")
    private Integer idMoneda;

    @Column(name = "codigo_moneda", nullable = false, unique = true, length = 5)
    private String codigoMoneda;

    @Column(name = "nombre_moneda", nullable = false, length = 40)
    private String nombreMoneda;

    @Column(name = "simbolo", length = 5)
    private String simbolo;
}
