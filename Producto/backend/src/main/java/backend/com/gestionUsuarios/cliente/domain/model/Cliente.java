package backend.com.gestionUsuarios.cliente.domain.model;

import backend.com.shared.domain.model.Contacto;
import backend.com.shared.domain.model.DatoBancario;
import backend.com.shared.domain.model.Direccion;
import backend.com.shared.domain.model.Giro;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    private Long clienteId;
    private String runCliente;
    private String razonSocial;
    private String horaAtencion;
    private boolean activo;
    private String sigla;

    private List<Contacto> contactos;
    private Giro giro;
    private List<Direccion> direcciones;
}
