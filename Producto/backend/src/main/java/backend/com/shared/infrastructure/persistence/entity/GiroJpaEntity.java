package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "giros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiroJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giro_id")
    private Long giroId;

    @Size(max = 255)
    @Column(name = "descripcion_giro", length = 255)
    private String descripcionGiro;

    @Size(max = 255)
    @Column(name = "codigo_actividad", length = 255)
    private String codigoActividad;

    @Size(max = 255)
    @Column(name = "tipo_actividad", length = 255)
    private String tipoActividad;

    @Size(max = 255)
    @Column(name = "categoria_tributaria", length = 255)
    private String categoriaTributaria;

    @Size(max = 255)
    @Column(name = "afecto_iva", length = 255)
    private String afectoIva;

    @Size(max = 255)
    @Column(name = "regimen_tributario", length = 255)
    private String regimenTributario;
}
