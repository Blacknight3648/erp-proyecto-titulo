package backend.com.shared.domain.model;

import backend.com.shared.domain.enums.TipoArticulo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Articulo {

    private Integer idArticulo;
    private String codigoArticulo;
    private String nombreArticulo;
    private String descripcionArticulo;
    private String codigoBarra;
    private TipoArticulo tipoArticulo;
    private Boolean activo;

    private CategoriaTela categoriaTela;
    private SubCategoriaTela subCategoriaTela;

    private ArticuloTela detalleTela;
    private ArticuloPrenda detallePrenda;
    private ArticuloAccesorio detalleAccesorio;
}
