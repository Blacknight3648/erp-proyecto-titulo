package backend.com.comercial.application.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CrearEVNCommand {
    // Número de documento (generado como timestamp si no se provee)
    private Long numero;

    // IDs de relaciones
    private Long clienteId;
    private Long vendedorId;
    private Long costeoId;
    private Long solicitudCotizacionId;

    // Datos del encabezado enviados desde el frontend
    private String clienteNombre;
    private String referencia;
    private String estado;

    // Costos adicionales (otros costos del negocio)
    private BigDecimal porcentajeComision;
    private BigDecimal garantiaSeriedad;
    private BigDecimal garantiaFielCumplimiento;
    private BigDecimal flete;
    private BigDecimal certificacion;
    private BigDecimal muestras;
    private BigDecimal entregaPersonalizada;
    private BigDecimal pegadoCinta;

    // Toma de tallaje
    private BigDecimal tomaTallajeMonto;
    private String tomaTallajeObservaciones;
    private String tomaTallajeMetadata; // JSON string

    private String pegadoCintaMetadata; // JSON string

    // Ítems de la evaluación
    private List<ItemEVNDTO> items;
}
