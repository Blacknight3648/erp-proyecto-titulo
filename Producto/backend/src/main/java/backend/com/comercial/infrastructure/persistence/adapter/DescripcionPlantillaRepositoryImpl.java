package backend.com.comercial.infrastructure.persistence.adapter;

import backend.com.comercial.domain.model.DescripcionPlantilla;
import backend.com.comercial.domain.repository.DescripcionPlantillaRepository;
import backend.com.comercial.infrastructure.mapper.DescripcionPlantillaMapper;
import backend.com.comercial.infrastructure.persistence.repository.DescripcionPlantillaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DescripcionPlantillaRepositoryImpl implements DescripcionPlantillaRepository {

    private final DescripcionPlantillaJpaRepository jpaRepository;
    private final DescripcionPlantillaMapper mapper;

    @Override
    public Optional<DescripcionPlantilla> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public List<DescripcionPlantilla> findByIdSCOS(Long idSCOS) {
        return jpaRepository.findByIdSCOS(idSCOS)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public DescripcionPlantilla save(DescripcionPlantilla descripcion) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(descripcion)));
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByIdSCOS(Long idSCOS) {
        jpaRepository.deleteByIdSCOS(idSCOS);
    }
}
