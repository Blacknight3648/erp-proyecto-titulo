package backend.com.gestionUsuarios.application.dto;

import backend.com.shared.application.dto.ContactoDTO;
import backend.com.shared.application.dto.DireccionDTO;
import backend.com.shared.application.dto.GiroDTO;
import backend.com.shared.validations.run.ValidRun;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

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

    @Size(max = 50, message = "La sigla no puede exceder 50 caracteres")
    private String sigla;

    private boolean activo;

    private GiroDTO giro;

    private List<ContactoDTO> contactos;
    private List<DireccionDTO> direcciones;
}
