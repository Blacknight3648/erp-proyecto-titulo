package backend.com.gestionUsuarios.proveedor.domain.model;

import backend.com.shared.domain.model.Giro;
import backend.com.shared.domain.model.Contacto;
import backend.com.shared.domain.model.Direccion;
import backend.com.shared.domain.model.DatoBancario;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    private Long proveedorId;
    private String runProveedor;
    private String razonSocialProveedor;
    private String sigla;
    private String tipoProveedor;
    private String horarioAtencion;
    private boolean activo;

    private List<Contacto> contactos;
    private List<Direccion> direccion;
    private List<DatoBancario> datosBancarios;
    private Giro giro;
}
