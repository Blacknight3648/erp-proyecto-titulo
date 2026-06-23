package backend.com.produccion.infrastructure.mapper;

import backend.com.produccion.domain.model.CosteoItemVersion;
import backend.com.produccion.domain.model.CosteoVersion;
import backend.com.produccion.infrastructure.persistence.entity.CosteoItemVersionJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.CosteoJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.CosteoVersionJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
import backend.com.shared.valueobjects.Money;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CosteoVersionMapper {

    private static final String DEFAULT_CURRENCY = "CLP";

    @PersistenceContext
    private EntityManager em;

    private final ArticuloRepository articuloRepository;

    public CosteoVersion toDomain(CosteoVersionJpaEntity entity) {
        if (entity == null)
            return null;

        List<CosteoItemVersion> items = entity.getItems() != null
                ? entity.getItems().stream().map(this::itemToDomain).collect(Collectors.toList())
                : new ArrayList<>();

        return new CosteoVersion(
                entity.getIdCosteoVersion(),
                entity.getCosteo() != null ? entity.getCosteo().getIdCosteo() : null,
                entity.getNumeroVersion(),
                entity.getFechaCreacion(),
                entity.getMotivoCambio(),
                entity.getUsuarioCreador(),
                items,
                toMoney(entity.getTotalManoObra()),
                toMoney(entity.getTotalHilo()),
                toMoney(entity.getTotalFlete()),
                toMoney(entity.getTotalEmbalaje()),
                toMoney(entity.getTotalEtiquetas()),
                entity.getPorcentajeCostoFijo(),
                toMoney(entity.getCostoTotalMateriaPrima()),
                entity.getMargenBrutoSugerido(),
                toMoney(entity.getPrecioVentaSugerido()));
    }

    public CosteoVersionJpaEntity toJpaEntity(CosteoVersion domain) {
        if (domain == null)
            return null;

        CosteoVersionJpaEntity entity = new CosteoVersionJpaEntity();
        entity.setIdCosteoVersion(domain.getIdCosteoVersion());
        if (domain.getCosteoId() != null) {
            entity.setCosteo(em.getReference(CosteoJpaEntity.class, domain.getCosteoId()));
        }
        entity.setNumeroVersion(domain.getNumeroVersion());
        entity.setFechaCreacion(domain.getFechaCreacion());
        entity.setMotivoCambio(domain.getMotivoCambio());
        entity.setUsuarioCreador(domain.getUsuarioCreador());
        entity.setTotalManoObra(toBigDecimal(domain.getTotalManoObra()));
        entity.setTotalHilo(toBigDecimal(domain.getTotalHilo()));
        entity.setTotalFlete(toBigDecimal(domain.getTotalFlete()));
        entity.setTotalEmbalaje(toBigDecimal(domain.getTotalEmbalaje()));
        entity.setTotalEtiquetas(toBigDecimal(domain.getTotalEtiquetas()));
        entity.setPorcentajeCostoFijo(domain.getPorcentajeCostoFijo());
        entity.setCostoTotalMateriaPrima(toBigDecimal(domain.getCostoTotalMateriaPrima()));
        entity.setMargenBrutoSugerido(domain.getMargenBrutoSugerido());
        entity.setPrecioVentaSugerido(toBigDecimal(domain.getPrecioVentaSugerido()));

        if (domain.getItems() != null) {
            List<CosteoItemVersionJpaEntity> itemsEntity = domain.getItems().stream()
                    .map(item -> {
                        CosteoItemVersionJpaEntity itemEntity = itemToJpaEntity(item);
                        itemEntity.setCosteoVersion(entity);
                        return itemEntity;
                    })
                    .collect(Collectors.toList());
            entity.setItems(itemsEntity);
        }

        return entity;
    }

    private CosteoItemVersion itemToDomain(CosteoItemVersionJpaEntity entity) {
        CosteoItemVersion domain = new CosteoItemVersion(
                entity.getIdCosteoItemVersion(),
                entity.getCosteoVersion() != null ? entity.getCosteoVersion().getIdCosteoVersion() : null,
                entity.getCosteoItemId() != null ? entity.getCosteoItemId() : null,
                entity.getTipoInsumo(),
                entity.getArticuloId(),
                entity.getNombreInsumo(),
                entity.getConsumo(),
                entity.getPrecioUnitario(),
                entity.getCostoTotal());
        // Lectura del artículo asociado (si existe). El snapshot mantiene su propio
        // nombreInsumo congelado; el artículo es solo informativo en lectura.
        if (entity.getArticuloId() != null) {
            articuloRepository.findById(entity.getArticuloId())
                    .ifPresent(domain::setArticulo);
        }
        return domain;
    }

    private CosteoItemVersionJpaEntity itemToJpaEntity(CosteoItemVersion domain) {
        CosteoItemVersionJpaEntity entity = new CosteoItemVersionJpaEntity();
        entity.setIdCosteoItemVersion(domain.getIdCosteoItemVersion());
        entity.setCosteoItemId(domain.getCosteoItemId());
        entity.setTipoInsumo(domain.getTipoInsumo());
        entity.setArticuloId(domain.getArticuloId());
        entity.setNombreInsumo(domain.getNombreInsumo());
        entity.setConsumo(domain.getConsumo());
        entity.setPrecioUnitario(domain.getPrecioUnitario());
        entity.setCostoTotal(domain.getCostoTotal());
        return entity;
    }

    private Money toMoney(BigDecimal amount) {
        return amount != null ? new Money(amount, DEFAULT_CURRENCY) : null;
    }

    private BigDecimal toBigDecimal(Money money) {
        return money != null ? money.getAmount() : null;
    }
}
