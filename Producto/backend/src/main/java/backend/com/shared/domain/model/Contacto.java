package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacto {

    private Long contactoId;
    private String nombreContacto;
    private String telefonoContacto;
    private String emailContacto;

    private TipoContacto tipoContacto;

}
