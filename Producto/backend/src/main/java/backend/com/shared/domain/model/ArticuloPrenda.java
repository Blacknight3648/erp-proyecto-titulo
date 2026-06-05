package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloPrenda {

    private Integer id;
    private String marca;
    private String tallasDisponibles;
    private String proveedor;
    private String codigoProveedor;
    private Boolean requiereLogoCliente;
    private Boolean tieneEstampado;
    private String ubicacionLogo;
}