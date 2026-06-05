package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.ClasificacionTecnica;
import backend.com.shared.infrastructure.mapper.ClasificacionTecnicaMapper;
import backend.com.shared.infrastructure.persistence.repository.ClasificacionTecnicaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.ClasificacionTecnicaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClasificacionTecnicaRepositoryImpl implements ClasificacionTecnicaRepository {

    private final ClasificacionTecnicaJpaRepository jpaRepository;
    private final ClasificacionTecnicaMapper mapper;

    @Override
    public List<ClasificacionTecnica> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<ClasificacionTecnica> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<ClasificacionTecnica> findByNombreClasificacionIgnoreCase(String nombreClasificacion) {
        return jpaRepository.findByNombreClasificacionIgnoreCase(nombreClasificacion).map(mapper::toDomain);
    }

    @Override
    public boolean existsByNombreClasificacion(String nombreClasificacion) {
        return jpaRepository.existsByNombreClasificacion(nombreClasificacion);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public ClasificacionTecnica save(ClasificacionTecnica clasificacion) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(clasificacion)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}