package backend.com.comercial.infrastructure.persistence.adapter;

import backend.com.comercial.domain.model.SolicitudCotizacion;
import backend.com.comercial.domain.repository.SolicitudCotizacionRepository;
import backend.com.comercial.infrastructure.persistence.entity.SolicitudCotizacionJpaEntity;
import backend.com.comercial.infrastructure.mapper.SolicitudCotizacionMapper;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCotizacionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SolicitudCotizacionRepositoryImpl implements SolicitudCotizacionRepository {

    private final SolicitudCotizacionJpaRepository jpaRepository;
    private final SolicitudCotizacionMapper mapper;

    @Override
    public SolicitudCotizacion save(SolicitudCotizacion solicitudCotizacion) {
        SolicitudCotizacionJpaEntity entity = mapper.toEntity(solicitudCotizacion);
        if (entity == null) {
            throw new IllegalArgumentException("La entidad de Solicitud de Cotización no puede ser nula");
        }
        SolicitudCotizacionJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public SolicitudCotizacion update(SolicitudCotizacion solicitudCotizacion) {
        SolicitudCotizacionJpaEntity entity = mapper.toEntity(solicitudCotizacion);
        if (entity == null) {
            throw new IllegalArgumentException("La entidad de Solicitud de Cotización no puede ser nula");
        }
        SolicitudCotizacionJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<SolicitudCotizacion> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<SolicitudCotizacion> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) {
            jpaRepository.deleteById(id);
        }
    }

    @Override
    public long countByTipo(String tipo){
        return jpaRepository.countByTipo(tipo);
    }
}
