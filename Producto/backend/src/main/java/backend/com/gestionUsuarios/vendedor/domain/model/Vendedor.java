package backend.com.gestionUsuarios.vendedor.domain.model;

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
    private Long id;
    private Long usuarioId; // Only ID to avoid circular dependencies in domain
    private String codigoVendedor;
    private Boolean activo;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
    
    // Derived or common fields for easier access in domain (optional)
    private String nombreCompleto;
}
