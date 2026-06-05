package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Composicion {

    private Integer idComposicion;
    private String codigoComposicion;
    private String descripcionComposicion;
    private String clasificacion;
    private String usoTipico;
}