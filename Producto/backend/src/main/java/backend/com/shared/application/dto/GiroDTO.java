package backend.com.shared.application.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiroDTO {

    private Long giroId;

    @Size(max = 255, message = "La descripción del giro no puede superar los 255 caracteres")
    private String descripcionGiro;

    @Size(max = 255, message = "El código de actividad no puede superar los 255 caracteres")
    private String codigoActividad;

    @Size(max = 255, message = "El tipo de actividad no puede superar los 255 caracteres")
    private String tipoActividad;

    @Size(max = 255, message = "La categoría tributaria no puede superar los 255 caracteres")
    private String categoriaTributaria;

    @Size(max = 255, message = "El afecto iva no puede superar los 255 caracteres")
    private String afectoIva;

    @Size(max = 255, message = "El régimen tributario no puede superar los 255 caracteres")
    private String regimenTributario;
}
