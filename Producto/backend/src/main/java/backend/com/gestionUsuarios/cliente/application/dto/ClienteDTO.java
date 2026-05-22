package backend.com.gestionUsuarios.cliente.application.dto;

import backend.com.shared.application.dto.DatoBancarioResponse;
import backend.com.shared.application.dto.DireccionResponse;
import backend.com.shared.application.dto.GiroDTO;
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
public class ClienteDTO {

    private Long clienteId;

    @NotBlank(message = "La razón social del cliente no puede estar vacía")
    @Size(max = 255, message = "La razón social no puede exceder 255 caracteres")
    private String razonSocial;

    @NotBlank(message = "El RUN del cliente es obligatorio")
    @ValidRun
    @Size(max = 12, message = "El RUN no puede exceder 12 caracteres")
    private String runCliente;

    @Email(message = "Debe ser un correo válido")
    @ValidEmail
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    private String correoCliente;

    @ValidPhone
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefonoCliente;

    @Size(max = 100, message = "El contacto no puede exceder 100 caracteres")
    private String contactoCliente;

    @Size(max = 50, message = "La sigla no puede exceder 50 caracteres")
    private String sigla;

    private boolean activo;

    private GiroDTO giro;

    // Para requests: poblar solo con { "direccionId": X }
    // Para responses: objeto completo mapeado desde dominio
    private DireccionResponse direccion;

    // Para requests: poblar solo con { "datoBancarioId": X }
    // Para responses: objeto completo mapeado desde dominio
    private DatoBancarioResponse datoBancario;
}
