package backend.com.produccion.application.dto;

import backend.com.produccion.domain.model.EstadoOP;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Vista consolidada de toda la cadena de trazabilidad partiendo de una OP:
 * NV → OP → CosteoVersion → HC → OCs → Recepciones OC, OSs (con despachos/recepciones)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrazabilidadOPDTO {

    private Long opId;
    private String numeroOP;
    private Long notaVentaId;
    private EstadoOP estadoOP;
    private LocalDate fechaEntregaProgramada;

    private CosteoVersionInfoDTO costeoVersion;

    private HojaCompraDTO hojaCompra;

    private List<OrdenCompraDTO> ordenesCompra;

    private List<RecepcionOCDTO> recepcionesOC;

    private List<OrdenServicioDTO> ordenesServicio;
}
