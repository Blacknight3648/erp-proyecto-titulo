package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "configuracion_plantillas")
@Getter
@Setter
public class ConfiguracionPlantillaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombrePrenda;

    @ElementCollection
    @CollectionTable(name = "configuracion_plantilla_campos", joinColumns = @JoinColumn(name = "configuracion_id"))
    @Column(name = "campo")
    private Set<String> camposActivos;

    @Column(name = "custom_fields", columnDefinition = "TEXT")
    private String customFields;

    @ElementCollection
    @CollectionTable(name = "configuracion_plantilla_telas", joinColumns = @JoinColumn(name = "configuracion_id"))
    private List<PlantillaTela> plantillaTelas = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "configuracion_plantilla_accesorios", joinColumns = @JoinColumn(name = "configuracion_id"))
    private List<PlantillaAccesorio> plantillaAccesorios = new ArrayList<>();
}
