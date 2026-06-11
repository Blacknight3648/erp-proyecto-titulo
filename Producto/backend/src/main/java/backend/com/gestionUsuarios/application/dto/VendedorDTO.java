package backend.com.gestionUsuarios.vendedor.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendedorDTO {
    private Long vendedorId;
    private Long usuarioId;
    private String nombreUsuario; // For display purposes
    private String apellidosUsuario;
    private String codigoVendedor;
    private Boolean activo;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}
