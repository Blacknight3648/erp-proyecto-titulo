package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CosteoDTO {
    private Long idCosteo;
    private String numeroCosteo;
    /** Estado del ciclo de vida: BORRADOR | COSTEADO | APROBADO | RECHAZADO. */
    private String estado;
    private String motivoRechazo;
    /** Iteración de reproceso (incrementa al retomar un costeo rechazado). */
    private Integer version;
    private Long solicitudCostosId;
    private Long clienteId;
    private String clienteNombre;
    private Long vendedorId;
    private String vendedorNombre;
    
    // Costos base
    private BigDecimal costoHilos;
    private BigDecimal costoManoObra;
    private BigDecimal costoEtiquetas;
    private BigDecimal costoEmbalaje;
    private BigDecimal costoFlete;
    private BigDecimal porcentajeCostoFijo;

    // Componentes variables
    private BigDecimal precioCinta1;
    private BigDecimal cantidadCinta1;
    private BigDecimal precioCinta2;
    private BigDecimal cantidadCinta2;
    private BigDecimal vivoReflectivo;
    private BigDecimal cantidadVivo;

    // Totales y sugerencias
    private BigDecimal costoTotalMateriaPrima;
    private BigDecimal margenBrutoSugerido;
    private BigDecimal precioVentaSugerido;
    
    private java.util.List<CosteoItemDTO> items;
}
