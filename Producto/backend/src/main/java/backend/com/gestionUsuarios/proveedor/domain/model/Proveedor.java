package backend.com.gestionUsuarios.proveedor.domain.model;

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

    private String nombreProveedor;

    private String rutProveedor;

    private String direccionProveedor;

    private String telefonoProveedor;

    private String emailProveedor;

    private String contactoProveedor;

    private String categoria;

    private boolean activo;
}
