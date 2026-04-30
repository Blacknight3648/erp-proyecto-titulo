package backend.com.comercial.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantillaAccesorioDTO {
    private String tipo;
    private String nombreAccesorio;
    private Integer cantidad;
}
