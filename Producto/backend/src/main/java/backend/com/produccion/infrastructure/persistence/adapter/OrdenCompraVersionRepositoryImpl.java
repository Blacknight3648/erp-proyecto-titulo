package backend.com.produccion.infrastructure.persistence.adapter;

import backend.com.produccion.domain.model.OrdenCompraVersion;
import backend.com.produccion.domain.repository.OrdenCompraVersionRepository;
import backend.com.produccion.infrastructure.mapper.OrdenCompraVersionMapper;
import backend.com.produccion.infrastructure.persistence.entity.OrdenCompraVersionJpaEntity;
import backend.com.produccion.infrastructure.persistence.repository.OrdenCompraVersionJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrdenCompraVersionRepositoryImpl implements OrdenCompraVersionRepository {

    private final OrdenCompraVersionJpaRepository jpaRepository;
    private final OrdenCompraVersionMapper mapper;

    @Override
    public OrdenCompraVersion save(OrdenCompraVersion ocVersion) {
        if (ocVersion == null) {
            throw new ValidationException("La OrdenCompraVersion no puede ser nula");
        }
        OrdenCompraVersionJpaEntity entity = mapper.toJpaEntity(ocVersion);
        if (entity == null) {
            throw new IllegalStateException("Error al mapear OrdenCompraVersion a entidad JPA");
        }
        OrdenCompraVersionJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<OrdenCompraVersion> findById(Long idOCVersion) {
        if (idOCVersion == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(idOCVersion).map(mapper::toDomain);
    }

    @Override
    public Optional<OrdenCompraVersion> findUltimaPorOcId(Long ocId) {
        if (ocId == null) {
            return Optional.empty();
        }
        return jpaRepository.findTopByOrdenCompra_IdOCOrderByNumeroVersionDesc(ocId)
                .map(mapper::toDomain);
    }

    @Override
    public List<OrdenCompraVersion> findAllByOcId(Long ocId) {
        if (ocId == null) {
            return List.of();
        }
        return jpaRepository.findByOrdenCompra_IdOCOrderByNumeroVersionAsc(ocId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Integer siguienteNumeroVersion(Long ocId) {
        if (ocId == null) {
            return 1;
        }
        return jpaRepository.findTopByOrdenCompra_IdOCOrderByNumeroVersionDesc(ocId)
                .map(v -> v.getNumeroVersion() + 1)
                .orElse(1);
    }
}
