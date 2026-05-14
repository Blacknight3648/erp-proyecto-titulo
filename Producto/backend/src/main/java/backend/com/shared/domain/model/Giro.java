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
    private String descripcionGiro;
    private String codigoActividad;
    private String tipoActividad;
    private String categoriaTributaria;
    private String afectoIva;
    private String regimenTributario;
}
