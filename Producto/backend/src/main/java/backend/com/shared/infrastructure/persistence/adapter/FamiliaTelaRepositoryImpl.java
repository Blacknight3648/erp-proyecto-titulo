package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.FamiliaTela;
import backend.com.shared.infrastructure.mapper.FamiliaTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.FamiliaTelaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.FamiliaTelaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FamiliaTelaRepositoryImpl implements FamiliaTelaRepository {

    private final FamiliaTelaJpaRepository jpaRepository;
    private final FamiliaTelaMapper mapper;

    @Override
    public List<FamiliaTela> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<FamiliaTela> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<FamiliaTela> findByCodigoFamilia(String codigoFamilia) {
        return jpaRepository.findByCodigoFamilia(codigoFamilia).map(mapper::toDomain);
    }

    @Override
    public Optional<FamiliaTela> findByNombreFamiliaIgnoreCase(String nombreFamilia) {
        return jpaRepository.findByNombreFamiliaIgnoreCase(nombreFamilia).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoFamilia(String codigoFamilia) {
        return jpaRepository.existsByCodigoFamilia(codigoFamilia);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public FamiliaTela save(FamiliaTela familia) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(familia)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}