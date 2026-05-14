package backend.com.gestionUsuarios.proveedor.domain.model;

import backend.com.shared.domain.model.Giro;
import backend.com.shared.domain.model.Sigla;
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
public class Proveedor {

    private Long proveedorId;
    private String runProveedor;
    private String razonSocialProveedor;
    private String direccionProveedor;
    private String contactoProveedor;
    private String emailProveedor;
    private String telefonoProveedor;
    private String tipoProveedor;
    private boolean activo;

    private Sigla sigla;
    private Giro giro;
}
