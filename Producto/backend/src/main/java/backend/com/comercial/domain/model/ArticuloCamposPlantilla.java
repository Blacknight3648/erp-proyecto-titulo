package backend.com.comercial.domain.model;

import backend.com.shared.domain.model.Articulo;
import lombok.*;
import java.util.List;

/**
 * Configuración que define qué Plantilla (campo) se sugiere por defecto
 * para un Articulo específico. Es un schema declarativo, NO validador
 * estricto — los usuarios pueden agregar o quitar campos por SCOS sin
 * que esto afecte al ModeloPlantilla.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloCamposPlantilla {

    private Long idModeloPlantilla;
    private Articulo articulo;
    /** Nombres de los campos de plantilla asignados al artículo. */
    private List<String> campos;
}
