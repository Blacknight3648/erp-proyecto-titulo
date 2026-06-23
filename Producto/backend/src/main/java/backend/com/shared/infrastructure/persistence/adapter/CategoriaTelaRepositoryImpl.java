package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.CategoriaTela;
import backend.com.shared.infrastructure.mapper.CategoriaTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.CategoriaTelaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.CategoriaTelaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoriaTelaRepositoryImpl implements CategoriaTelaRepository {

    private final CategoriaTelaJpaRepository jpaRepository;
    private final CategoriaTelaMapper mapper;

    @Override
    public List<CategoriaTela> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<CategoriaTela> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<CategoriaTela> findByCodigoCategoriaTela(String codigoCategoriaTela) {
        return jpaRepository.findByCodigoCategoriaTela(codigoCategoriaTela).map(mapper::toDomain);
    }

    @Override
    public Optional<CategoriaTela> findByNombreCategoriaTela(String nombreCategoriaTela) {
        return jpaRepository.findByNombreCategoriaTela(nombreCategoriaTela).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoCategoriaTela(String codigoCategoriaTela) {
        return jpaRepository.existsByCodigoCategoriaTela(codigoCategoriaTela);
    }

    @Override
    public boolean existsByNombreCategoriaTela(String nombreCategoriaTela) {
        return jpaRepository.existsByNombreCategoriaTela(nombreCategoriaTela);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public CategoriaTela save(CategoriaTela categoriaTela) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(categoriaTela)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}
