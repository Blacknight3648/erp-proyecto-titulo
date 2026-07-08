package backend.com.shared.domain.model;

import backend.com.shared.domain.enums.TipoDatoAtributoAccesorio;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtributoAccesorioDefinicion {

    private Integer idDefinicion;
    private TipoAccesorio tipoAccesorio;
    private String nombreCampo;
    private TipoDatoAtributoAccesorio tipoDato;
    private String opciones;
    private Integer orden;
    private Boolean requerido;
}
