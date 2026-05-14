package backend.com.gestionUsuarios.proveedor.application.dto;

import backend.com.shared.application.dto.GiroDTO;
import backend.com.shared.application.dto.SiglaDTO;
import backend.com.shared.validations.email.ValidEmail;
import backend.com.shared.validations.run.ValidRun;
import backend.com.shared.validations.telefono.ValidPhone;
import jakarta.validation.constraints.Email;
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

    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    private String direccionProveedor;

    @Size(max = 150, message = "El contacto no puede exceder 150 caracteres")
    private String contactoProveedor;

    @Email(message = "El correo debe ser válido")
    @ValidEmail
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
    private String emailProveedor;

    @ValidPhone
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefonoProveedor;

    @Size(max = 30, message = "El tipo de proveedor no puede exceder 30 caracteres")
    private String tipoProveedor;

    private boolean activo;

    private SiglaDTO sigla;
    private GiroDTO giro;
}
