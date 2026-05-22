package backend.com.shared.application.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactoDTO {

    private Long idContacto;
    private String nombreContacto;
    private String telefonoContacto;
    private String emailContacto;

    private TipoContactoDTO tipoContacto;
}
