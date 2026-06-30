package backend.com.produccion.infrastructure.mapper;

import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.dto.CosteoItemDTO;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.CosteoItem;
import backend.com.produccion.infrastructure.persistence.entity.CosteoJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.CosteoItemJpaEntity;
import backend.com.shared.infrastructure.mapper.ArticuloMapper;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CosteoMapper {

    private final ArticuloMapper articuloMapper;

    public Costeo toDomain(CosteoJpaEntity entity) {
        if (entity == null)
            return null;

        Costeo c = new Costeo(
                entity.getIdCosteo(),
                entity.getNumeroCosteo() != null ? new DocumentNumber(entity.getNumeroCosteo()) : null,
                entity.getSolicitudCostosId(),
                null, // clienteId (populated in service)
                null, // clienteNombre (populated in service)
                null, // vendedorId (populated in service)
                null, // vendedorNombre (populated in service)
                new Money(entity.getCostoHilos() != null ? entity.getCostoHilos() : BigDecimal.ZERO, "CLP"),
                new Money(entity.getCostoManoObra() != null ? entity.getCostoManoObra() : BigDecimal.ZERO, "CLP"),
                entity.getObservacionesManoObra(),
                new Money(entity.getCostoEtiquetas() != null ? entity.getCostoEtiquetas() : BigDecimal.ZERO, "CLP"),
                new Money(entity.getCostoEmbalaje() != null ? entity.getCostoEmbalaje() : BigDecimal.ZERO, "CLP"),
                new Money(entity.getCostoFlete() != null ? entity.getCostoFlete() : BigDecimal.ZERO, "CLP"),
                entity.getPorcentajeCostoFijo() != null ? entity.getPorcentajeCostoFijo() : BigDecimal.ZERO,
                new Money(entity.getPrecioCinta1() != null ? entity.getPrecioCinta1() : BigDecimal.ZERO, "CLP"),
                entity.getCantidadCinta1() != null ? entity.getCantidadCinta1() : BigDecimal.ZERO,
                new Money(entity.getPrecioCinta2() != null ? entity.getPrecioCinta2() : BigDecimal.ZERO, "CLP"),
                entity.getCantidadCinta2() != null ? entity.getCantidadCinta2() : BigDecimal.ZERO,
                new Money(entity.getVivoReflectivo() != null ? entity.getVivoReflectivo() : BigDecimal.ZERO, "CLP"),
                entity.getCantidadVivo() != null ? entity.getCantidadVivo() : BigDecimal.ZERO,
                new Money(entity.getCostoTotalMateriaPrima() != null ? entity.getCostoTotalMateriaPrima() : BigDecimal.ZERO, "CLP"),
                entity.getMargenBrutoSugerido() != null ? entity.getMargenBrutoSugerido() : BigDecimal.ZERO,
                new Money(entity.getPrecioVentaSugerido() != null ? entity.getPrecioVentaSugerido() : BigDecimal.ZERO, "CLP"),
                entity.getItems() != null ? entity.getItems().stream().map(this::mapItemToDomain).collect(Collectors.toList()) : new ArrayList<>());
        c.setNotaVentaId(entity.getNotaVentaId());
        c.setEstado(entity.getEstado() != null ? entity.getEstado() : backend.com.produccion.domain.enums.EstadoCosteo.BORRADOR);
        c.setMotivoRechazo(entity.getMotivoRechazo());
        c.setVersion(entity.getVersion() != null ? entity.getVersion() : 1);
        return c;
    }

    public CosteoJpaEntity toJpaEntity(Costeo domain) {
        if (domain == null)
            return null;

        CosteoJpaEntity entity = new CosteoJpaEntity();
        entity.setIdCosteo(domain.getIdCosteo());
        entity.setNumeroCosteo(domain.getNumeroCosteo() != null ? domain.getNumeroCosteo().getValue() : null);
        entity.setEstado(domain.getEstado() != null ? domain.getEstado() : backend.com.produccion.domain.enums.EstadoCosteo.BORRADOR);
        entity.setMotivoRechazo(domain.getMotivoRechazo());
        entity.setVersion(domain.getVersion() != null ? domain.getVersion() : 1);
        entity.setSolicitudCostosId(domain.getSolicitudCostosId());
        entity.setNotaVentaId(domain.getNotaVentaId());
        entity.setCostoHilos(domain.getCostoHilos().getAmount());
        entity.setCostoManoObra(domain.getCostoManoObra().getAmount());
        entity.setObservacionesManoObra(domain.getObservacionesManoObra());
        entity.setCostoEtiquetas(domain.getCostoEtiquetas().getAmount());
        entity.setCostoEmbalaje(domain.getCostoEmbalaje().getAmount());
        entity.setCostoFlete(domain.getCostoFlete().getAmount());
        entity.setPorcentajeCostoFijo(domain.getPorcentajeCostoFijo());
        entity.setPrecioCinta1(domain.getPrecioCinta1().getAmount());
        entity.setCantidadCinta1(domain.getCantidadCinta1());
        entity.setPrecioCinta2(domain.getPrecioCinta2().getAmount());
        entity.setCantidadCinta2(domain.getCantidadCinta2());
        entity.setVivoReflectivo(domain.getVivoReflectivo().getAmount());
        entity.setCantidadVivo(domain.getCantidadVivo());
        entity.setCostoTotalMateriaPrima(domain.getCostoTotalMateriaPrima().getAmount());
        entity.setMargenBrutoSugerido(domain.getMargenBrutoSugerido());
        entity.setPrecioVentaSugerido(domain.getPrecioVentaSugerido().getAmount());

        if (domain.getItems() != null) {
            entity.setItems(domain.getItems().stream().map(i -> {
                CosteoItemJpaEntity itemEntity = mapItemToJpaEntity(i);
                itemEntity.setCosteo(entity);
                return itemEntity;
            }).collect(Collectors.toList()));
        }

        return entity;
    }

    public CosteoDTO toDto(Costeo domain) {
        if (domain == null) return null;
        return CosteoDTO.builder()
                .idCosteo(domain.getIdCosteo())
                .numeroCosteo(domain.getNumeroCosteo() != null ? domain.getNumeroCosteo().getValue() : null)
                .estado(domain.getEstado() != null ? domain.getEstado().name() : null)
                .motivoRechazo(domain.getMotivoRechazo())
                .version(domain.getVersion())
                .solicitudCostosId(domain.getSolicitudCostosId())
                .clienteId(domain.getClienteId())
                .clienteNombre(domain.getClienteNombre())
                .vendedorId(domain.getVendedorId())
                .vendedorNombre(domain.getVendedorNombre())
                .costoHilos(domain.getCostoHilos().getAmount())
                .costoManoObra(domain.getCostoManoObra().getAmount())
                .observacionesManoObra(domain.getObservacionesManoObra())
                .costoEtiquetas(domain.getCostoEtiquetas().getAmount())
                .costoEmbalaje(domain.getCostoEmbalaje().getAmount())
                .costoFlete(domain.getCostoFlete().getAmount())
                .porcentajeCostoFijo(domain.getPorcentajeCostoFijo())
                .precioCinta1(domain.getPrecioCinta1().getAmount())
                .cantidadCinta1(domain.getCantidadCinta1())
                .precioCinta2(domain.getPrecioCinta2().getAmount())
                .cantidadCinta2(domain.getCantidadCinta2())
                .vivoReflectivo(domain.getVivoReflectivo().getAmount())
                .cantidadVivo(domain.getCantidadVivo())
                .costoTotalMateriaPrima(domain.getCostoTotalMateriaPrima().getAmount())
                .margenBrutoSugerido(domain.getMargenBrutoSugerido())
                .precioVentaSugerido(domain.getPrecioVentaSugerido().getAmount())
                .items(domain.getItems().stream().map(this::mapItemToDto).collect(Collectors.toList()))
                .build();
    }

    public Costeo toDomainFromDto(CosteoDTO dto) {
        if (dto == null) return null;
        Costeo costeo = new Costeo(
                dto.getIdCosteo(),
                dto.getNumeroCosteo() != null ? new DocumentNumber(dto.getNumeroCosteo()) : null,
                dto.getSolicitudCostosId(),
                dto.getClienteId(),
                dto.getClienteNombre(),
                dto.getVendedorId(),
                dto.getVendedorNombre(),
                new Money(dto.getCostoHilos() != null ? dto.getCostoHilos() : BigDecimal.ZERO, "CLP"),
                new Money(dto.getCostoManoObra() != null ? dto.getCostoManoObra() : BigDecimal.ZERO, "CLP"),
                dto.getObservacionesManoObra(),
                new Money(dto.getCostoEtiquetas() != null ? dto.getCostoEtiquetas() : BigDecimal.ZERO, "CLP"),
                new Money(dto.getCostoEmbalaje() != null ? dto.getCostoEmbalaje() : BigDecimal.ZERO, "CLP"),
                new Money(dto.getCostoFlete() != null ? dto.getCostoFlete() : BigDecimal.ZERO, "CLP"),
                dto.getPorcentajeCostoFijo() != null ? dto.getPorcentajeCostoFijo() : BigDecimal.ZERO,
                new Money(dto.getPrecioCinta1() != null ? dto.getPrecioCinta1() : BigDecimal.ZERO, "CLP"),
                dto.getCantidadCinta1() != null ? dto.getCantidadCinta1() : BigDecimal.ZERO,
                new Money(dto.getPrecioCinta2() != null ? dto.getPrecioCinta2() : BigDecimal.ZERO, "CLP"),
                dto.getCantidadCinta2() != null ? dto.getCantidadCinta2() : BigDecimal.ZERO,
                new Money(dto.getVivoReflectivo() != null ? dto.getVivoReflectivo() : BigDecimal.ZERO, "CLP"),
                dto.getCantidadVivo() != null ? dto.getCantidadVivo() : BigDecimal.ZERO,
                new Money(dto.getCostoTotalMateriaPrima() != null ? dto.getCostoTotalMateriaPrima() : BigDecimal.ZERO, "CLP"),
                dto.getMargenBrutoSugerido() != null ? dto.getMargenBrutoSugerido() : BigDecimal.ZERO,
                new Money(dto.getPrecioVentaSugerido() != null ? dto.getPrecioVentaSugerido() : BigDecimal.ZERO, "CLP"),
                dto.getItems() != null ? dto.getItems().stream().map(this::mapItemToDomainFromDto).collect(Collectors.toList()) : new ArrayList<>()
        );
        // Estado: respeta el del DTO si viene; si no, queda el default BORRADOR del dominio.
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            costeo.setEstado(backend.com.produccion.domain.enums.EstadoCosteo.valueOf(dto.getEstado()));
        }
        costeo.setMotivoRechazo(dto.getMotivoRechazo());
        if (dto.getVersion() != null) {
            costeo.setVersion(dto.getVersion());
        }
        return costeo;
    }

    private CosteoItem mapItemToDomain(CosteoItemJpaEntity entity) {
        return CosteoItem.builder()
                .idCosteoItem(entity.getIdCosteoItem())
                .tipoInsumo(entity.getTipoInsumo())
                .articuloId(entity.getArticuloId())
                .nombreInsumo(entity.getNombreInsumo())
                .consumo(entity.getConsumo())
                .precioUnitario(entity.getPrecioUnitario())
                .costoTotal(entity.getCostoTotal())
                .build();
    }

    private CosteoItemJpaEntity mapItemToJpaEntity(CosteoItem domain) {
        return CosteoItemJpaEntity.builder()
                .idCosteoItem(domain.getIdCosteoItem())
                .tipoInsumo(domain.getTipoInsumo())
                .articuloId(domain.getArticuloId())
                .nombreInsumo(domain.getNombreInsumo())
                .consumo(domain.getConsumo())
                .precioUnitario(domain.getPrecioUnitario())
                .costoTotal(domain.getCostoTotal())
                .build();
    }

    private CosteoItemDTO mapItemToDto(CosteoItem domain) {
        return CosteoItemDTO.builder()
                .idCosteoItem(domain.getIdCosteoItem())
                .tipoInsumo(domain.getTipoInsumo())
                .articuloId(domain.getArticuloId())
                .nombreInsumo(domain.getNombreInsumo())
                .consumo(domain.getConsumo())
                .precioUnitario(domain.getPrecioUnitario())
                .costoTotal(domain.getCostoTotal())
                .articulo(articuloMapper.toDTO(domain.getArticulo()))
                .build();
    }

    private CosteoItem mapItemToDomainFromDto(CosteoItemDTO dto) {
        return CosteoItem.builder()
                .idCosteoItem(dto.getIdCosteoItem())
                .tipoInsumo(dto.getTipoInsumo())
                .articuloId(dto.getArticuloId())
                .nombreInsumo(dto.getNombreInsumo())
                .consumo(dto.getConsumo())
                .precioUnitario(dto.getPrecioUnitario())
                .costoTotal(dto.getCostoTotal())
                .build();
    }
}
