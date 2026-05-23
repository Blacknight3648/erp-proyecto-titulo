package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Direccion {

    private Long direccionId;
    private String calle;
    private String numero;
    private String depto;
    private TipoDireccion tipoDireccion;
    private Comuna comuna;

}
