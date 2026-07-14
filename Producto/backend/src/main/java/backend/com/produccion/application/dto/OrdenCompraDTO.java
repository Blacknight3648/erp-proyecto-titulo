package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import backend.com.produccion.domain.enums.EstadoOC;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdenCompraDTO {

    private Long idOC;
    private String numeroOC;
    private Long proveedorId;
    private EstadoOC estado;
    private LocalDate fechaEmision;
    private LocalDate fechaEntregaEstimada;
    private String observaciones;
    private BigDecimal totalNeto;
    private List<OrdenCompraItemDTO> items;
    private String motivoRechazo;
    private LocalDateTime fechaRechazo;
    private Integer version;
}
