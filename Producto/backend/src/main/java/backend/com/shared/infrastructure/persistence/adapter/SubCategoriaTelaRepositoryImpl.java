package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.SubCategoriaTela;
import backend.com.shared.infrastructure.mapper.SubCategoriaTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.SubCategoriaTelaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.SubCategoriaTelaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubCategoriaTelaRepositoryImpl implements SubCategoriaTelaRepository {

    private final SubCategoriaTelaJpaRepository jpaRepository;
    private final SubCategoriaTelaMapper mapper;

    @Override
    public List<SubCategoriaTela> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<SubCategoriaTela> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<SubCategoriaTela> findByCodigoSubCategoriaTela(String codigoSubCategoriaTela) {
        return jpaRepository.findByCodigoSubCategoriaTela(codigoSubCategoriaTela).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoSubCategoriaTela(String codigoSubCategoriaTela) {
        return jpaRepository.existsByCodigoSubCategoriaTela(codigoSubCategoriaTela);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public List<SubCategoriaTela> findByCategoriaTelajId(Integer idCategoriaTela) {
        return jpaRepository.findByCategoriaTela_IdCategoriaTela(idCategoriaTela)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public SubCategoriaTela save(SubCategoriaTela subCategoriaTela) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(subCategoriaTela)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}
