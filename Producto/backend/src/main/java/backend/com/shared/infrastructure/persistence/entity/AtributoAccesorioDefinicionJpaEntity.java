package backend.com.shared.infrastructure.persistence.entity;

import backend.com.shared.domain.enums.TipoDatoAtributoAccesorio;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "atributo_accesorio_definicion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtributoAccesorioDefinicionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_definicion")
    private Integer idDefinicion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_accesorio", nullable = false)
    private TipoAccesorioJpaEntity tipoAccesorio;

    @Column(name = "nombre_campo", nullable = false, length = 40)
    private String nombreCampo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_dato", nullable = false, length = 20)
    private TipoDatoAtributoAccesorio tipoDato;

    /** Opciones válidas separadas por "|" — solo aplica cuando tipoDato = LISTA. */
    @Column(name = "opciones", length = 500)
    private String opciones;

    @Builder.Default
    @Column(name = "orden", nullable = false)
    private Integer orden = 0;

    @Builder.Default
    @Column(name = "requerido", nullable = false)
    private Boolean requerido = false;
}
