package backend.com.shared.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloAccesorio {

    private Integer id;
    private TipoAccesorio tipoAccesorio;
    private List<AtributoAccesorioValor> atributos;
    private String proveedor;
    private String codigoProveedor;
    private Boolean requiereLogoCliente;
}