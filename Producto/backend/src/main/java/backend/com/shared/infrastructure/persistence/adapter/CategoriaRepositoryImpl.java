package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Categoria;
import backend.com.shared.infrastructure.mapper.CategoriaMapper;
import backend.com.shared.infrastructure.persistence.repository.CategoriaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.CategoriaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoriaRepositoryImpl implements CategoriaRepository {

    private final CategoriaJpaRepository jpaRepository;
    private final CategoriaMapper mapper;

    @Override
    public List<Categoria> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Categoria> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Categoria> findByCodigoCategoria(String codigoCategoria) {
        return jpaRepository.findByCodigoCategoria(codigoCategoria).map(mapper::toDomain);
    }

    @Override
    public Optional<Categoria> findByNombreCategoria(String nombreCategoria) {
        return jpaRepository.findByNombreCategoria(nombreCategoria).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoCategoria(String codigoCategoria) {
        return jpaRepository.existsByCodigoCategoria(codigoCategoria);
    }

    @Override
    public boolean existsByNombreCategoria(String nombreCategoria) {
        return jpaRepository.existsByNombreCategoria(nombreCategoria);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public Categoria save(Categoria categoria) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(categoria)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}