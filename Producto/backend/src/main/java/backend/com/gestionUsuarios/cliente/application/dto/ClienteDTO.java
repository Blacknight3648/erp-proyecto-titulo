package backend.com.gestionUsuarios.cliente.application.dto;

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
    private String nombreCliente;
    private String apellidoCliente;
    private String runCliente;
    private String correoCliente;
    private String telefonoCliente;
    private String direccionCliente;
    private String segmento;
    private String contacto;
    private boolean activo;

}