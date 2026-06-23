package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnidadMedida {

    private Integer idUnidadMedida;
    private String nombreUnidad;
    private String abreviatura;
}