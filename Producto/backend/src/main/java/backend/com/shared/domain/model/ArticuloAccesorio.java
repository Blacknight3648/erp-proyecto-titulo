package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloAccesorio {

    private Integer id;
    private String subtipoAccesorio;
    private String tallasDisponibles;
    private String proveedor;
    private String codigoProveedor;
    private Boolean requiereLogoCliente;
}