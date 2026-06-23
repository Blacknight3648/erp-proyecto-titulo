package backend.com.comercial.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudCostosDTO {
    private Long id;
    private String numero;
    private String estado;
    private String tipo;
    private Long clienteId;
    private String clienteNombre;
    private Long vendedorId;
    private String vendedorNombre;
    private String articuloDescripcion;
    private String nombrePrenda;
    private Boolean esMuestra;
    private Boolean hasLogo;
    private Integer cantidad;
    private String genero;
    private String tallaje;
    private LocalDate fecha;
    private BigDecimal costoTotal;
    private String moneda;
    private List<SCOSTelaDTO> telas;
    private List<SCOSAccesorioDTO> accesorios;
    private List<SCOSLogotipoDTO> logotipos;
    private List<DescripcionPlantillaDTO> descripciones;
}
