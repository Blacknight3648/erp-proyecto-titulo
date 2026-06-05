package backend.com.shared.application.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloPrendaDTO {

    private Integer id;
    private String marca;
    private String tallasDisponibles;
    private String proveedor;
    private String codigoProveedor;
    private Boolean requiereLogoCliente;
    private Boolean tieneEstampado;
    private String ubicacionLogo;
}