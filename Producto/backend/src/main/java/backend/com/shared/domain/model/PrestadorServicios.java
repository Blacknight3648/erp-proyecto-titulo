package backend.com.shared.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestadorServicios {

    private long prestadorServiciosId;
    private String runPrestadorServicios;
    private String razonSocialPrestador;

    private Contacto contacto;
    private TipoServicio tipoServicio;

}
