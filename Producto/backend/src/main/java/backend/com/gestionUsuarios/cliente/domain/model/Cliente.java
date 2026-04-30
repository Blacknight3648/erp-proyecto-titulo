package backend.com.gestionUsuarios.cliente.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
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