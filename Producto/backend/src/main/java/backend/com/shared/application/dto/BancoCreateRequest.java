package backend.com.maestros.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BancoCreateRequest {

    private Integer idBanco;
    private String empresa;
    private String nombreBanco;
    private String codigoBanco;

}
