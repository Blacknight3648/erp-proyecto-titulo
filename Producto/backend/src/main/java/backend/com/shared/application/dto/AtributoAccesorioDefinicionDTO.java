package backend.com.shared.application.dto;

import backend.com.shared.domain.enums.TipoDatoAtributoAccesorio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtributoAccesorioDefinicionDTO {

    private Integer idDefinicion;

    @NotNull(message = "El tipo de accesorio es obligatorio")
    private TipoAccesorioDTO tipoAccesorio;

    @NotBlank(message = "El nombre del campo es obligatorio")
    @Size(max = 40)
    private String nombreCampo;

    @NotNull(message = "El tipo de dato es obligatorio")
    private TipoDatoAtributoAccesorio tipoDato;

    /** Opciones válidas — solo aplica cuando tipoDato = LISTA. */
    private List<String> opciones;

    private Integer orden;
    private Boolean requerido;
}
