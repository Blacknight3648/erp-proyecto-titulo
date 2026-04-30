package backend.com.shared.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    private Long productoId;

    private String codigoProducto;

    private String nombreProducto;

    private String descripcionProducto;

    private String generoProducto;

    private String colorProducto;

    private String modeloProducto;

    private String telaProducto;

    private String composicionProducto;

    private String gramajeProducto;
}
