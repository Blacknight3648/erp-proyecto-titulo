package backend.com.comercial.infrastructure.persistence.adapter;

import backend.com.comercial.domain.model.ArticuloCamposPlantilla;
import backend.com.comercial.domain.repository.ArticuloCamposPlantillaRepository;
import backend.com.comercial.infrastructure.mapper.ArticuloCamposPlantillaMapper;
import backend.com.comercial.infrastructure.persistence.repository.ArticuloCamposPlantillaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ArticuloCamposPlantillaRepositoryImpl implements ArticuloCamposPlantillaRepository {

    private final ArticuloCamposPlantillaJpaRepository jpaRepository;
    private final ArticuloCamposPlantillaMapper mapper;

    @Override
    public List<ArticuloCamposPlantilla> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<ArticuloCamposPlantilla> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<ArticuloCamposPlantilla> findByArticuloId(Integer idArticulo) {
        if (idArticulo == null) return Optional.empty();
        return jpaRepository.findByArticulo_IdArticulo(idArticulo).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public ArticuloCamposPlantilla save(ArticuloCamposPlantilla modelo) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(modelo)));
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByArticuloId(Integer idArticulo) {
        jpaRepository.deleteByArticulo_IdArticulo(idArticulo);
    }
}
