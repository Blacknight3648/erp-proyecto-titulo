package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "atributo_tecnico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtributoTecnicoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atributo")
    private Integer idAtributo;

    @Column(name = "codigo_atributo", nullable = false, unique = true, length = 10)
    private String codigoAtributo;

    @Column(name = "clasificacion", length = 20)
    private String clasificacion;

    @Column(name = "descripcion_tecnica", nullable = false, length = 60)
    private String descripcionTecnica;

    @Column(name = "impacto_erp", length = 100)
    private String impactoErp;
}