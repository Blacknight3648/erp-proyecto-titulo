package backend.com.produccion.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Cuerpo de solicitud para reingresar una OC RECHAZADA. Permite corregir el
 * proveedor y editar cantidad/precio de ítems en el mismo paso (motivo
 * típico de rechazo: proveedor sin stock o precio/cantidad incorrectos).
 */
@Data
public class ReingresarOCRequest {

    @NotBlank(message = "El aprobador es obligatorio para firmar la acción")
    @Size(max = 100)
    private String aprobador;

    @Size(max = 100)
    private String rol;

    private Long proveedorId;

    private List<OrdenCompraItemDTO> itemsCambiados;
}
