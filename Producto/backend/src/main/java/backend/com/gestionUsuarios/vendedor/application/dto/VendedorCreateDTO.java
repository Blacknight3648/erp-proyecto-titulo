package backend.com.gestionUsuarios.vendedor.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendedorCreateDTO {
    
    @NotNull(message = "El id de usuario es obligatorio")
    private Long usuarioId;
    
    @NotBlank(message = "El código de vendedor es obligatorio")
    private String codigoVendedor;
}
