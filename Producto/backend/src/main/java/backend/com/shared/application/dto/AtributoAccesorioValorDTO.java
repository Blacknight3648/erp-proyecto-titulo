package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtributoAccesorioValorDTO {

    private Integer idValor;

    @NotNull(message = "La definición del atributo es obligatoria")
    private Integer idDefinicion;

    /** Solo lectura — nombre del campo, para mostrar en listas sin otro fetch. */
    private String nombreCampo;

    private String valorTexto;

    private Integer idColorReferencia;

    /** Solo lectura — descripción del color referenciado. */
    private String colorDescripcion;
}
