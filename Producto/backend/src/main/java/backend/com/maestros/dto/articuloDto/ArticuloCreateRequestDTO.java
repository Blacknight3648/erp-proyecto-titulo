package backend.com.maestros.dto.articuloDto;

import backend.com.maestros.domain.entity.ArticuloAccesorio;
import backend.com.maestros.domain.entity.ArticuloPrenda;
import backend.com.maestros.domain.entity.ArticuloTela;
import backend.com.maestros.domain.enums.TipoArticulo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloCreateRequestDTO {

    @NotBlank(message = "El código de artículo es obligatorio")
    @Size(max = 20, message = "El código no puede superar 20 caracteres")
    private String codigoArticulo;

    @NotBlank(message = "El nombre del artículo es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombreArticulo;

    @Size(max = 200, message = "La descripción no puede superar 200 caracteres")
    private String descripcionArticulo;

    @Size(max = 50, message = "El código de barra no puede superar 50 caracteres")
    private String codigoBarra;

    @NotNull(message = "El tipo de artículo es obligatorio")
    private TipoArticulo tipoArticulo;

    @Size(max = 1, message = "La procedencia debe ser un carácter")
    private String procedencia;

    private Boolean lotes;
    private Boolean seriales;
    private Boolean compras;
    private Integer comision;

    @NotNull(message = "La categoría es obligatoria")
    private Integer idCategoria;

    private Integer idSubCategoria;

    @NotNull(message = "La unidad de medida es obligatoria")
    private Integer idUnidadMedida;

    private ArticuloTela    detalleTela;
    private ArticuloPrenda  detallePrenda;
    private ArticuloAccesorio detalleAccesorio;
}
