package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Comuna;
import backend.com.shared.domain.repository.ComunaRepository;
import backend.com.shared.infrastructure.mapper.ComunaMapper;
import backend.com.shared.infrastructure.persistence.repository.ComunaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ComunaRepositoryImpl implements ComunaRepository {

    private final ComunaJpaRepository jpaRepository;
    private final ComunaMapper mapper;

    @Override
    public List<Comuna> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Comuna> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Comuna> findByRegionId(Long regionId) {
        return jpaRepository.findByRegion_RegionId(regionId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Comuna> findByNombreComuna(String nombreComuna) {
        return jpaRepository.findByNombreComuna(nombreComuna).map(mapper::toDomain);
    }

    @Override
    public Comuna save(Comuna comuna) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(comuna)));
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return id != null && jpaRepository.existsById(id);
    }
}
