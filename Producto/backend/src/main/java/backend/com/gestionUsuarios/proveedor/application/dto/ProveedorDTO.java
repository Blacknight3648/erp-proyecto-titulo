package backend.com.gestionUsuarios.proveedor.application.dto;

import backend.com.shared.validations.email.ValidEmail;
import backend.com.shared.validations.run.ValidRun;
import backend.com.shared.validations.telefono.ValidPhone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorDTO {

    private Long proveedorId;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(max = 100)
    private String nombreProveedor;

    @ValidRun
    @NotBlank(message = "El RUN del cliente es obligatorio")
    private String rutProveedor;

    @Size(max = 150)
    @NotBlank(message = "La dirección del proveedor no puede estar vacía")
    private String direccionProveedor;

    @ValidPhone
    @NotBlank(message = "El número de télefono del proveedor no puede estar vacío")
    private String telefonoProveedor;

    @ValidEmail
    private String emailProveedor;

    @Size(max = 100)
    private String contactoProveedor;

    @Size(max = 100)
    @NotBlank(message = "Debes seleccionar una categoría")
    private String categoria;

    private boolean activo;

}
