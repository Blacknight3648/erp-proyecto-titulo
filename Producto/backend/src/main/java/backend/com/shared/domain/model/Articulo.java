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
    private String procedencia;
    private Boolean lotes;
    private Boolean seriales;
    private Boolean compras;
    private Integer comision;
    private Boolean activo;

    private Categoria categoria;
    private SubCategoria subCategoria;
    private UnidadMedida unidadMedida;

    private ArticuloTela detalleTela;
    private ArticuloPrenda detallePrenda;
    private ArticuloAccesorio detalleAccesorio;
}