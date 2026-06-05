package backend.com.shared.application.dto;

import backend.com.shared.domain.enums.TipoArticulo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO unificado de Artículo. Se usa tanto para crear como para actualizar.
 * En PUT, el campo {@code codigoArticulo} se ignora (no se permite renombrar el código).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloDTO {

    private Integer idArticulo;

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
    private Boolean activo;

    @NotNull(message = "La categoría es obligatoria")
    private Integer idCategoria;

    private Integer idSubCategoria;

    @NotNull(message = "La unidad de medida es obligatoria")
    private Integer idUnidadMedida;

    private ArticuloTelaDTO detalleTela;
    private ArticuloPrendaDTO detallePrenda;
    private ArticuloAccesorioDTO detalleAccesorio;
}