package backend.com.produccion.infrastructure.mapper;

import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.dto.CosteoItemDTO;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.CosteoItem;
import backend.com.produccion.infrastructure.persistence.entity.CosteoJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.CosteoItemJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CosteoMapper {

    public Costeo toDomain(CosteoJpaEntity entity) {
        if (entity == null)
            return null;

        return new Costeo(
                entity.getIdCosteo(),
                entity.getNumeroCosteo() != null ? new DocumentNumber(entity.getNumeroCosteo()) : null,
                entity.getSolicitudCostosId(),
                null, // clienteId (populated in service)
                null, // clienteNombre (populated in service)
                null, // vendedorId (populated in service)
                null, // vendedorNombre (populated in service)
                new Money(entity.getCostoHilos(), "CLP"),
                new Money(entity.getCostoManoObra(), "CLP"),
                new Money(entity.getCostoEtiquetas(), "CLP"),
                new Money(entity.getCostoEmbalaje(), "CLP"),
                new Money(entity.getCostoFlete(), "CLP"),
                entity.getPorcentajeCostoFijo(),
                new Money(entity.getPrecioCinta1(), "CLP"),
                entity.getCantidadCinta1(),
                new Money(entity.getPrecioCinta2(), "CLP"),
                entity.getCantidadCinta2(),
                new Money(entity.getVivoReflectivo(), "CLP"),
                entity.getCantidadVivo(),
                new Money(entity.getCostoTotalMateriaPrima(), "CLP"),
                entity.getMargenBrutoSugerido(),
                new Money(entity.getPrecioVentaSugerido(), "CLP"),
                entity.getItems() != null ? entity.getItems().stream().map(this::mapItemToDomain).collect(Collectors.toList()) : new ArrayList<>());
    }

    public CosteoJpaEntity toJpaEntity(Costeo domain) {
        if (domain == null)
            return null;

        CosteoJpaEntity entity = new CosteoJpaEntity();
        entity.setIdCosteo(domain.getIdCosteo());
        entity.setNumeroCosteo(domain.getNumeroCosteo() != null ? domain.getNumeroCosteo().getValue() : null);
        entity.setSolicitudCostosId(domain.getSolicitudCostosId());
        entity.setCostoHilos(domain.getCostoHilos().getAmount());
        entity.setCostoManoObra(domain.getCostoManoObra().getAmount());
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
                .solicitudCostosId(domain.getSolicitudCostosId())
                .clienteId(domain.getClienteId())
                .clienteNombre(domain.getClienteNombre())
                .vendedorId(domain.getVendedorId())
                .vendedorNombre(domain.getVendedorNombre())
                .costoHilos(domain.getCostoHilos().getAmount())
                .costoManoObra(domain.getCostoManoObra().getAmount())
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
        return new Costeo(
                dto.getIdCosteo(),
                dto.getNumeroCosteo() != null ? new DocumentNumber(dto.getNumeroCosteo()) : null,
                dto.getSolicitudCostosId(),
                dto.getClienteId(),
                dto.getClienteNombre(),
                dto.getVendedorId(),
                dto.getVendedorNombre(),
                new Money(dto.getCostoHilos() != null ? dto.getCostoHilos() : BigDecimal.ZERO, "CLP"),
                new Money(dto.getCostoManoObra() != null ? dto.getCostoManoObra() : BigDecimal.ZERO, "CLP"),
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
    }

    private CosteoItem mapItemToDomain(CosteoItemJpaEntity entity) {
        return CosteoItem.builder()
                .idCosteoItem(entity.getIdCosteoItem())
                .tipoInsumo(entity.getTipoInsumo())
                .insumoId(entity.getInsumoId())
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
                .insumoId(domain.getInsumoId())
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
                .insumoId(domain.getInsumoId())
                .nombreInsumo(domain.getNombreInsumo())
                .consumo(domain.getConsumo())
                .precioUnitario(domain.getPrecioUnitario())
                .costoTotal(domain.getCostoTotal())
                .build();
    }

    private CosteoItem mapItemToDomainFromDto(CosteoItemDTO dto) {
        return CosteoItem.builder()
                .idCosteoItem(dto.getIdCosteoItem())
                .tipoInsumo(dto.getTipoInsumo())
                .insumoId(dto.getInsumoId())
                .nombreInsumo(dto.getNombreInsumo())
                .consumo(dto.getConsumo())
                .precioUnitario(dto.getPrecioUnitario())
                .costoTotal(dto.getCostoTotal())
                .build();
    }
}
