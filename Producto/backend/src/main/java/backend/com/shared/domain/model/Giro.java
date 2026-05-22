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
public class Giro {

    private Long giroId;
    private String codigoSii;
    private String nombreGiro;
    private String descripcionGiro;

    private Rubro rubro;
}
