package backend.com.produccion.application.dto;

import backend.com.produccion.domain.model.EstadoOS;
import backend.com.produccion.domain.model.TipoServicioOS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdenServicioDTO {

    private Long idOS;
    private String numeroOS;
    private Long opId;
    private Long proveedorId;
    private TipoServicioOS tipoServicio;
    private EstadoOS estado;
    private LocalDate fechaEmision;
    private LocalDate fechaEntregaEstimada;
    private String descripcionTrabajo;
    private Integer cantidadPactada;
    private BigDecimal precioUnitario;
    private BigDecimal totalNeto;
    private String observaciones;
    private List<DespachoOSDTO> despachos;
    private List<RecepcionOSDTO> recepciones;
}
