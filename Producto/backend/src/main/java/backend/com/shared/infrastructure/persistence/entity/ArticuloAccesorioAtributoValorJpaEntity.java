package backend.com.shared.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Valor concreto de un atributo dinámico de accesorio para un artículo
 * puntual (ej. para el Cierre "CIE-004": Tipo=DP, Terminal=Fijo, Medida=15cm,
 * Color→referencia a color_tela).
 */
@Entity
@Table(name = "articulo_accesorio_atributo_valor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloAccesorioAtributoValorJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valor")
    private Integer idValor;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_articulo", nullable = false)
    private ArticuloAccesorioJpaEntity articuloAccesorio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_definicion", nullable = false)
    private AtributoAccesorioDefinicionJpaEntity definicion;

    @Column(name = "valor_texto", length = 100)
    private String valorTexto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_color_referencia")
    private ColorTelaJpaEntity colorReferencia;
}
