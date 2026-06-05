package backend.com.shared.application.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloAccesorioDTO {

    private Integer id;
    private String subtipoAccesorio;
    private String tallasDisponibles;
    private String proveedor;
    private String codigoProveedor;
    private Boolean requiereLogoCliente;
}