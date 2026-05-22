package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoServicio {

    private Long tipoServicioId;
    private String descripcionTipoServicio;

}
