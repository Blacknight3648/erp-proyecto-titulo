package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoContacto {

    private Long tipoContactoId;
    private String descripcionTipoContacto;

}
