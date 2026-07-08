package backend.com.shared.application.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloAccesorioDTO {

    private Integer id;
    private TipoAccesorioDTO tipoAccesorio;
    private List<AtributoAccesorioValorDTO> atributos;
    private String proveedor;
    private String codigoProveedor;
    private Boolean requiereLogoCliente;
}