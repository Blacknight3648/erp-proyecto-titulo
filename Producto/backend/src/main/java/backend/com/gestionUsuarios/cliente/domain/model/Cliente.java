package backend.com.gestionUsuarios.cliente.domain.model;

import backend.com.shared.domain.model.Giro;

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
public class Cliente {

    private Long clienteId;
    private String runCliente;
    private String razonSocial;
    private String correoCliente;
    private String telefonoCliente;
    private String direccionCliente;
    private String contactoCliente;
    private boolean activo;
    private String sigla;

    private Giro giro;

}
