package backend.com.comercial.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CrearNVCommand {
    private Long numero;
    private Long evaluacionNegocioId;
    private Long clienteId;
    private Long vendedorId;
    private Boolean esKit;
    private String detalleKit;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaEntregaEstimada;
    private List<ItemNVDTO> items;
}
