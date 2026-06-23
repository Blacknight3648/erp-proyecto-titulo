package backend.com.gestionUsuarios.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendedor {
    private Long vendedorId;
    private Long usuarioId; // Only ID to avoid circular dependencies in domain
    private String codigoVendedor;
    private Boolean activo;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
    
    private String nombreCompleto;
}
