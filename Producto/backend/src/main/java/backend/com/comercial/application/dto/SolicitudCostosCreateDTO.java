package backend.com.comercial.application.dto;

import java.util.List;
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
public class SolicitudCostosCreateDTO {
    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    private Long vendedorId;

    @NotBlank(message = "La descripción del artículo es obligatoria")
    @NotNull(message = "La descripción del artículo es obligatoria")
    private String articuloDescripcion;
    private String nombrePrenda;

    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;

    @NotNull(message = "El género es obligatorio")
    private String genero;

    private String tallaje;

    private List<SCOSTelaDTO> telas;
    private List<SCOSAccesorioDTO> accesorios;
    private List<SCOSLogotipoDTO> logotipos;
    private List<DescripcionPlantillaDTO> descripciones;

    private String tipo;
    private String estado;
    private Boolean esMuestra;
    private Boolean hasLogo;
    private java.math.BigDecimal costoTotal;

    private Boolean esPrendaNueva;
    private List<String> camposPlantilla;
}
