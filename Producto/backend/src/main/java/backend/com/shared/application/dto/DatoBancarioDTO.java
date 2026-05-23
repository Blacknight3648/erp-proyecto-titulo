package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatoBancarioDTO {

    private Integer datoBancarioId;

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(max = 50, message = "El número de cuenta no puede superar 50 caracteres")
    private String numeroCuenta;

    private BancoDTO banco;

    private TipoCuentaBancariaDTO tipoCuentaBancaria;
}