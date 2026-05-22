package backend.com.gestionUsuarios.proveedor.domain.model;

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
public class Proveedor {

    private Long proveedorId;
    private String runProveedor;
    private String razonSocialProveedor;
    private String sigla;
    private String tipoProveedor;
    private String horarioAtencion;
    private boolean activo;

    private Giro giro;
}
