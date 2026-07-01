package backend.com.comercial.application.dto;

import backend.com.comercial.domain.model.NotaVenta;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class NVResponse {

    private Long idNV;
    private String numeroNV;
    private Long evaluacionNegocioId;
    private Long clienteId;
    private String clienteNombre;
    private Long vendedorId;
    private String estado;
    private Boolean esKit;
    private String detalleKit;
    private LocalDate fechaEmision;
    private LocalDate fechaEntregaEstimada;
    private BigDecimal montoSubtotal;
    private BigDecimal montoIva;
    private BigDecimal montoTotal;
    private List<ItemNVResponse> items;

    @Data
    public static class ItemNVTallaResponse {
        private String talla;
        private Integer cantidad;
    }

    @Data
    public static class ItemNVResponse {
        private Long idItemNV;
        private Integer nroItem;
        private Integer articuloId;
        private String nombreProducto;
        private String modelo;
        private String tela;
        private String composicion;
        private String color;
        private String talla;
        private String genero;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal total;
        private Boolean requiereOt;
        private String detalleOt;
        private String tipoItem;
        private Long proveedorId;
        private String nombreProveedor;
        private String llevaLogo;
        private String logoDetalle;
        private List<ItemNVTallaResponse> tallas;
    }

    public static NVResponse fromDomain(NotaVenta domain) {
        NVResponse response = new NVResponse();
        response.setIdNV(domain.getIdNV());
        response.setNumeroNV(domain.getNumeroNV() != null ? domain.getNumeroNV().getValue() : null);
        response.setEvaluacionNegocioId(domain.getEvaluacionNegocioId());
        response.setClienteId(domain.getClienteId());
        response.setClienteNombre("Cliente #" + domain.getClienteId()); // Placeholder consistente
        response.setVendedorId(domain.getVendedorId());
        response.setEstado(domain.getEstado().name());
        response.setEsKit(domain.getEsKit());
        response.setDetalleKit(domain.getDetalleKit());
        response.setFechaEmision(domain.getFechaEmision());
        response.setFechaEntregaEstimada(domain.getFechaEntregaEstimada());
        response.setMontoSubtotal(domain.getMontoSubtotal()!= null ? domain.getMontoSubtotal().getAmount() : BigDecimal.ZERO);
        response.setMontoIva(domain.getMontoIva()!= null ? domain.getMontoIva().getAmount() : BigDecimal.ZERO);
        response.setMontoTotal(domain.getMontoTotal()!= null ? domain.getMontoTotal().getAmount() : BigDecimal.ZERO);

        response.setItems(domain.getItems().stream().map(item -> {
            ItemNVResponse itemResponse = new ItemNVResponse();
            itemResponse.setIdItemNV(item.getIdItemNV());
            itemResponse.setNroItem(item.getNroItem());
            itemResponse.setArticuloId(item.getArticuloId());
            itemResponse.setNombreProducto(item.getNombreProducto());
            itemResponse.setModelo(item.getModelo());
            itemResponse.setTela(item.getTela());
            itemResponse.setComposicion(item.getComposicion());
            itemResponse.setColor(item.getColor());
            itemResponse.setTalla(item.getTalla());
            itemResponse.setGenero(item.getGenero());
            itemResponse.setCantidad(item.getCantidad());
            itemResponse.setPrecioUnitario(item.getPrecioUnitario()!= null ? item.getPrecioUnitario().getAmount() : BigDecimal.ZERO);
            itemResponse.setTotal(item.getTotal()!= null ? item.getTotal().getAmount() : BigDecimal.ZERO);
            itemResponse.setRequiereOt(item.getRequiereOt());
            itemResponse.setDetalleOt(item.getDetalleOt());
            itemResponse.setTipoItem(item.getTipoItem() != null ? item.getTipoItem().name() : null);
            itemResponse.setProveedorId(item.getProveedorId());
            itemResponse.setNombreProveedor(item.getNombreProveedor());
            itemResponse.setLlevaLogo(item.getLlevaLogo());
            itemResponse.setLogoDetalle(item.getLogoDetalle());
            if (item.getTallas() != null && !item.getTallas().isEmpty()) {
                itemResponse.setTallas(item.getTallas().stream().map(t -> {
                    ItemNVTallaResponse tr = new ItemNVTallaResponse();
                    tr.setTalla(t.getTalla());
                    tr.setCantidad(t.getCantidad());
                    return tr;
                }).collect(Collectors.toList()));
            }
            return itemResponse;
        }).collect(Collectors.toList()));

        return response;
    }
}
