package backend.com.gestionUsuarios.proveedor.application.dto;

import backend.com.shared.application.dto.GiroDTO;
import backend.com.shared.validations.run.ValidRun;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorDTO {

    private Long proveedorId;

    @NotBlank(message = "El RUN del proveedor es obligatorio")
    @ValidRun
    @Size(max = 12, message = "El RUN no puede exceder 12 caracteres")
    private String runProveedor;

    @NotBlank(message = "La razón social del proveedor es obligatoria")
    @Size(max = 255, message = "La razón social no puede exceder 255 caracteres")
    private String razonSocialProveedor;

    @Size(max = 30, message = "El tipo de proveedor no puede exceder 30 caracteres")
    private String tipoProveedor;

    @Size(max = 30, message = "El horario de atención no puede exceder 30 caracteres")
    private String horarioAtencion;

    @Size(max = 100, message = "La sigla del proveedor no puede exceder 100 caracteres")
    private String sigla;

    private boolean activo;

    private GiroDTO giro;
}
