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

    @Size(max = 255, message = "El código de actividad no puede superar los 255 caracteres")
    private String codigoSii;

    @Size(max = 255, message = "El nombre del giro no puede superar los 255 caracteres")
    private String nombreGiro;

    @Size(max = 255, message = "La descripción del giro no puede superar los 255 caracteres")
    private String descripcionGiro;

    private RubroDTO rubro;
}
