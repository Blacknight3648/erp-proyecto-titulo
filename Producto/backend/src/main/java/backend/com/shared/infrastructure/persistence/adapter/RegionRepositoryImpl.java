package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Region;
import backend.com.shared.infrastructure.persistence.repository.RegionRepository;
import backend.com.shared.infrastructure.mapper.RegionMapper;
import backend.com.shared.infrastructure.persistence.repository.Jpa.RegionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RegionRepositoryImpl implements RegionRepository {

    private final RegionJpaRepository jpaRepository;
    private final RegionMapper mapper;

    @Override
    public List<Region> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Region> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Region> findByPaisId(Integer paisId) {
        return jpaRepository.findByPais_PaisId(paisId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Region> findByNombreRegion(String nombreRegion) {
        return jpaRepository.findByNombreRegion(nombreRegion).map(mapper::toDomain);
    }

    @Override
    public Region save(Region region) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(region)));
    }

    @Override
    public void deleteById(Long id) {
        if (id != null)
            jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return id != null && jpaRepository.existsById(id);
    }
}
