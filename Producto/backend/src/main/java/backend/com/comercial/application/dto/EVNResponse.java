package backend.com.comercial.application.dto;

import backend.com.comercial.domain.enums.TipoItem;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.GastoAdicional;
import backend.com.comercial.domain.model.TomaTallajeDetalle;
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
    private List<backend.com.comercial.domain.model.GastoAdicionalDetalle> pegadoCintaDetalles;

    @Data
    public static class ItemEVNResponse {
        private Integer articuloId;
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
        private BigDecimal costoProducto;
        private BigDecimal costoLogo;
        private BigDecimal costoOrdenTrabajo;
        private BigDecimal costoTotalUnitario;
        private TipoItem tipoItem;
        private BigDecimal totalItem;
        private BigDecimal margenItem;
        private List<backend.com.comercial.domain.model.ItemEspecificacion> technicalSpecs;
        private Long costeoId;
        private Long solicitudCostosId;
    }

    @Data
    public static class GastoAdicionalResponse {
        private String tipoGasto;
        private BigDecimal monto;
        private String moneda;
        private List<backend.com.comercial.domain.model.GastoAdicionalDetalle> detalles;
    }

    @Data
    public static class TomaTallajeResponse {
        private BigDecimal costoTotal;
        private String moneda;
        private String observaciones;
        private LocalDate fechaProgramada;
        private Integer diasXRecinto;
        private Integer persXRecinto;
        private BigDecimal colaccionXPers;
        private BigDecimal asignacionXPers;
        private BigDecimal peajes;
        private BigDecimal bencinaXLt;
        private BigDecimal kmTotal;
        private BigDecimal rendKmLt;
        private Integer recintos;
        private BigDecimal personal;
        private BigDecimal movilizacion;
        private List<TomaTallajeDetalle> detalles;
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

        domain.getGastosAdicionales().stream()
                .filter(g -> g.getTipoGasto() == GastoAdicional.TipoGastoAdicional.PEGADO_CINTA)
                .findFirst()
                .ifPresent(g -> response.setPegadoCintaDetalles(g.getDetalles()));

        response.setItems(domain.getItems().stream().map(item -> {
            ItemEVNResponse r = new ItemEVNResponse();
            r.setArticuloId(item.getArticuloId());
            r.setProveedorId(item.getProveedorId());
            r.setNroItem(item.getNroItem());
            r.setDescripcion(item.getDescripcion());
            r.setModelo(item.getModelo());
            r.setTela(item.getTela());
            r.setComposicion(item.getComposicion());
            r.setGenero(item.getGenero());
            r.setCodigoInterno(item.getCodigoInterno());
            r.setCodigoProveedor(item.getCodigoProveedor());
            r.setProveedorNombre(item.getProveedorNombre());
            r.setCantidad(item.getCantidad());
            r.setPrecioUnitario(item.getPrecioUnitario().getAmount());
            r.setCostoUnitario(item.getCostoUnitario().getAmount());
            r.setCostoProducto(item.getCostoProducto());
            r.setCostoLogo(item.getCostoLogo());
            r.setCostoOrdenTrabajo(item.getCostoOrdenTrabajo());
            r.setCostoTotalUnitario(item.getCostoTotalUnitario());
            r.setTipoItem(item.getTipoItem());
            r.setTotalItem(item.getTotal().getAmount());
            r.setMargenItem(item.getMargenItem());
            r.setTechnicalSpecs(item.getTechnicalSpecs());
            r.setCosteoId(item.getCosteoId());
            r.setSolicitudCostosId(item.getSolicitudCostosId());
            return r;
        }).collect(Collectors.toList()));

        if (domain.getGastosAdicionales() != null) {
            response.setGastosAdicionales(domain.getGastosAdicionales().stream().map(gasto -> {
                GastoAdicionalResponse gr = new GastoAdicionalResponse();
                gr.setTipoGasto(gasto.getTipoGasto().name());
                gr.setMonto(gasto.getMonto().getAmount());
                gr.setMoneda(gasto.getMonto().getCurrency());
                gr.setDetalles(gasto.getDetalles());
                return gr;
            }).collect(Collectors.toList()));
        }

        if (domain.getTomaTallaje() != null) {
            backend.com.comercial.domain.model.TomaTallaje tt = domain.getTomaTallaje();
            TomaTallajeResponse ttR = new TomaTallajeResponse();
            ttR.setCostoTotal(tt.getCostoTotal().getAmount());
            ttR.setMoneda(tt.getCostoTotal().getCurrency());
            ttR.setObservaciones(tt.getObservaciones());
            ttR.setFechaProgramada(tt.getFechaProgramada());
            ttR.setDiasXRecinto(tt.getDiasXRecinto());
            ttR.setPersXRecinto(tt.getPersXRecinto());
            ttR.setColaccionXPers(tt.getColaccionXPers());
            ttR.setAsignacionXPers(tt.getAsignacionXPers());
            ttR.setPeajes(tt.getPeajes());
            ttR.setBencinaXLt(tt.getBencinaXLt());
            ttR.setKmTotal(tt.getKmTotal());
            ttR.setRendKmLt(tt.getRendKmLt());
            ttR.setRecintos(tt.getRecintos());
            ttR.setPersonal(tt.calcPersonal());
            ttR.setMovilizacion(tt.calcMovilizacion());
            ttR.setDetalles(tt.getDetalles());
            response.setTomaTallaje(ttR);
        }

        return response;
    }
}
