package backend.com.comercial.application.dto;

import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.GastoAdicional;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EVNResponse {
    private Long id;
    private String numero;
    private Long clienteId;
    private Long vendedorId;
    private String estado;
    private LocalDate fechaEvaluacion;
    private BigDecimal montoTotal;
    private BigDecimal margenGanancia;
    private BigDecimal rentabilidadEsperada;
    private BigDecimal porcentajeComision;
    private String referencia;
    private String clienteNombre;
    private String vendedorNombre;
    private List<ItemEVNResponse> items;
    private List<GastoAdicionalResponse> gastosAdicionales;
    private TomaTallajeResponse tomaTallaje;
    private String tomaTallajeMetadata;
    private String pegadoCintaMetadata;

    @Data
    public static class ItemEVNResponse {
        private Long productoId;
        private Long proveedorId;
        private Integer nroItem;
        private String descripcion;
        private String modelo;
        private String tela;
        private String composicion;
        private String genero;
        private String codigoInterno;
        private String codigoProveedor;
        private String proveedorNombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal costoUnitario;
        private String tipoItem;
        private BigDecimal totalItem;
        private BigDecimal costoLogo;
        private BigDecimal costoOrdenTrabajo;
        private BigDecimal margenItem;
        private java.util.Map<String, String> technicalSpecs;
    }

    @Data
    public static class GastoAdicionalResponse {
        private String tipoGasto;
        private BigDecimal monto;
        private String moneda;
    }

    @Data
    public static class TomaTallajeResponse {
        private BigDecimal monto;
        private String moneda;
        private String observaciones;
        private LocalDate fechaProgramada;
        private String metadata;
    }

    public static EVNResponse fromDomain(EvaluacionNegocio domain) {
        EVNResponse response = new EVNResponse();
        response.setId(domain.getEvaluacionNegocioId());
        response.setNumero(domain.getNumeroEvn().getValue());
        response.setClienteId(domain.getClienteId());
        response.setVendedorId(domain.getVendedorId());
        response.setEstado(domain.getEstado().name());
        response.setFechaEvaluacion(domain.getFechaEvaluacion());
        response.setMontoTotal(domain.getMontoTotal().getAmount());
        response.setMargenGanancia(domain.getMargenGanancia());
        response.setRentabilidadEsperada(domain.getRentabilidadEsperada());
        response.setPorcentajeComision(domain.getPorcentajeComision());
        response.setReferencia(domain.getReferencia());
        response.setClienteNombre(domain.getClienteNombre());
        response.setVendedorNombre(domain.getVendedorNombre());
        
        if (domain.getTomaTallaje() != null) {
            response.setTomaTallajeMetadata(domain.getTomaTallaje().getMetadataJson());
        }
        
        domain.getGastosAdicionales().stream()
            .filter(g -> g.getTipoGasto() == GastoAdicional.TipoGastoAdicional.PEGADO_CINTA)
            .findFirst()
            .ifPresent(g -> response.setPegadoCintaMetadata(g.getMetadataJson()));

        response.setItems(domain.getItems().stream().map(item -> {
            ItemEVNResponse itemResponse = new ItemEVNResponse();
            itemResponse.setProductoId(item.getProductoId());
            itemResponse.setProveedorId(item.getProveedorId());
            itemResponse.setCantidad(item.getCantidad());
            itemResponse.setPrecioUnitario(item.getPrecioUnitario().getAmount());
            itemResponse.setCostoUnitario(item.getCostoUnitario().getAmount());
            itemResponse.setCostoLogo(item.getCostoLogo());
            itemResponse.setCostoOrdenTrabajo(item.getCostoOrdenTrabajo());
            itemResponse.setTipoItem(item.getTipoItem());
            itemResponse.setTotalItem(item.getTotal().getAmount());
            itemResponse.setMargenItem(item.getMargenItem());
            itemResponse.setTechnicalSpecs(item.getTechnicalSpecs());

            // Mapear campos descriptivos desde el mapa si están presentes
            if (item.getTechnicalSpecs() != null) {
                itemResponse.setDescripcion(item.getTechnicalSpecs().get("descripcion"));
                itemResponse.setModelo(item.getTechnicalSpecs().get("modelo"));
                itemResponse.setTela(item.getTechnicalSpecs().get("tela"));
                itemResponse.setComposicion(item.getTechnicalSpecs().get("composicion"));
                itemResponse.setGenero(item.getTechnicalSpecs().get("genero"));
                itemResponse.setCodigoInterno(item.getTechnicalSpecs().get("codigoInterno"));
                itemResponse.setProveedorNombre(item.getTechnicalSpecs().get("proveedor"));
            }
            return itemResponse;
        }).collect(Collectors.toList()));

        if (domain.getGastosAdicionales() != null) {
            response.setGastosAdicionales(domain.getGastosAdicionales().stream().map(gasto -> {
                GastoAdicionalResponse gastoResponse = new GastoAdicionalResponse();
                gastoResponse.setTipoGasto(gasto.getTipoGasto().name());
                gastoResponse.setMonto(gasto.getMonto().getAmount());
                gastoResponse.setMoneda(gasto.getMonto().getCurrency());
                return gastoResponse;
            }).collect(Collectors.toList()));
        }

        if (domain.getTomaTallaje() != null) {
            TomaTallajeResponse ttResponse = new TomaTallajeResponse();
            ttResponse.setMonto(domain.getTomaTallaje().getCostoTotal().getAmount());
            ttResponse.setMoneda(domain.getTomaTallaje().getCostoTotal().getCurrency());
            ttResponse.setObservaciones(domain.getTomaTallaje().getObservaciones());
            ttResponse.setFechaProgramada(domain.getTomaTallaje().getFechaProgramada());
            ttResponse.setMetadata(domain.getTomaTallaje().getMetadataJson());
            response.setTomaTallaje(ttResponse);
        }

        return response;
    }
}
