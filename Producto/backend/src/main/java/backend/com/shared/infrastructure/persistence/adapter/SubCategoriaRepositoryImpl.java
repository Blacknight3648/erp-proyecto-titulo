package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.SubCategoria;
import backend.com.shared.infrastructure.mapper.SubCategoriaMapper;
import backend.com.shared.infrastructure.persistence.repository.SubCategoriaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.SubCategoriaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubCategoriaRepositoryImpl implements SubCategoriaRepository {

    private final SubCategoriaJpaRepository jpaRepository;
    private final SubCategoriaMapper mapper;

    @Override
    public List<SubCategoria> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<SubCategoria> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<SubCategoria> findByCodigoSubcategoria(String codigoSubcategoria) {
        return jpaRepository.findByCodigoSubcategoria(codigoSubcategoria).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoSubcategoria(String codigoSubcategoria) {
        return jpaRepository.existsByCodigoSubcategoria(codigoSubcategoria);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public List<SubCategoria> findByCategoriaId(Integer idCategoria) {
        return jpaRepository.findByCategoria_IdCategoria(idCategoria)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public SubCategoria save(SubCategoria subCategoria) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(subCategoria)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}