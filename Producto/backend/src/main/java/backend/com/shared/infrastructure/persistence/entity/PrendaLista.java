package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prenda_lista")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PrendaLista extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prendaListaId;

    @Column(nullable = false, length = 100)
    private String nombrePrenda;

    @Column(length = 200)
    private String descripcion;

    @Column(length = 50)
    private String genero;

    @Column(length = 50)
    private String color;

    @Column(length = 50)
    private String modelo;

    @Column(length = 50)
    private String tela;

    @Column(length = 50)
    private String composicion;

    @Column(length = 50)
    private String gramaje;
}
