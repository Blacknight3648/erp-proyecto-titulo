package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.GramajeTela;
import backend.com.shared.infrastructure.mapper.GramajeTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.GramajeTelaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.GramajeTelaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GramajeTelaRepositoryImpl implements GramajeTelaRepository {

    private final GramajeTelaJpaRepository jpaRepository;
    private final GramajeTelaMapper mapper;

    @Override
    public List<GramajeTela> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<GramajeTela> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<GramajeTela> findByCodigoGramaje(String codigoGramaje) {
        return jpaRepository.findByCodigoGramaje(codigoGramaje).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoGramaje(String codigoGramaje) {
        return jpaRepository.existsByCodigoGramaje(codigoGramaje);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public List<GramajeTela> findByCategoriaVestuarioIgnoreCase(String categoriaVestuario) {
        return jpaRepository.findByCategoriaVestuarioIgnoreCase(categoriaVestuario)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public GramajeTela save(GramajeTela gramaje) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(gramaje)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}