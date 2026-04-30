package backend.com.comercial.application.dto;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudCotizacionesCreateDTO {
    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    private Long vendedorId;

    private String articuloDescripcion;

    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;

    private List<SCOTPrendaListaDTO> prendas;
    private List<SCOSLogotipoDTO> logotipos;

    private String tipo;
    private String estado;
    private Boolean esMuestra;
    private Boolean hasLogo;

    private SCOSCostoFijoDTO costoFijo;
}
