package backend.com.comercial.application.dto;

import backend.com.comercial.domain.model.GastoAdicionalDetalle;
import backend.com.comercial.domain.model.TomaTallajeDetalle;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CrearEVNCommand {
    private Long numero;

    private Long clienteId;
    private Long vendedorId;

    private String clienteNombre;
    private String referencia;
    private String estado;

    private BigDecimal porcentajeComision;

    // Gastos adicionales
    private BigDecimal garantiaSeriedad;
    private BigDecimal garantiaFielCumplimiento;
    private BigDecimal flete;
    private BigDecimal certificacion;
    private BigDecimal muestras;
    private BigDecimal entregaPersonalizada;
    private BigDecimal pegadoCinta;
    private List<GastoAdicionalDetalle> pegadoCintaDetalles;

    // Toma de tallaje — inputs para el cálculo (3FN)
    private String tomaTallajeObservaciones;
    private Integer tomaTallajeDiasXRecinto;
    private Integer tomaTallajePersonasXRecinto;
    private BigDecimal tomaTallajeColaccionXPers;
    private BigDecimal tomaTallajeAsignacionXPers;
    private BigDecimal tomaTallajePeajes;
    private BigDecimal tomaTallajeBencinaXLt;
    private BigDecimal tomaTallajeKmTotal;
    private BigDecimal tomaTallajeRendKmLt;
    private Integer tomaTallajeRecintos;
    private List<TomaTallajeDetalle> tomaTallajeDetalles;

    // Ítems de la evaluación
    private List<ItemEVNDTO> items;
}
