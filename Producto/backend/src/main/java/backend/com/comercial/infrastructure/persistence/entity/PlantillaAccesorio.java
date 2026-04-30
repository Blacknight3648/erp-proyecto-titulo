package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaAccesorio {
    private String tipo;
    private String nombreAccesorio;
    private Integer cantidad;
}
