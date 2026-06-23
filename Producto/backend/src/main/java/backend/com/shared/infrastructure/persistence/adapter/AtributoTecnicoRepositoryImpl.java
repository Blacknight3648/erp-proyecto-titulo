package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.AtributoTecnico;
import backend.com.shared.infrastructure.mapper.AtributoTecnicoMapper;
import backend.com.shared.infrastructure.persistence.repository.AtributoTecnicoRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.AtributoTecnicoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AtributoTecnicoRepositoryImpl implements AtributoTecnicoRepository {

    private final AtributoTecnicoJpaRepository jpaRepository;
    private final AtributoTecnicoMapper mapper;

    @Override
    public List<AtributoTecnico> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<AtributoTecnico> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<AtributoTecnico> findByCodigoAtributo(String codigoAtributo) {
        return jpaRepository.findByCodigoAtributo(codigoAtributo).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoAtributo(String codigoAtributo) {
        return jpaRepository.existsByCodigoAtributo(codigoAtributo);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public List<AtributoTecnico> findByClasificacionIgnoreCase(String clasificacion) {
        return jpaRepository.findByClasificacionIgnoreCase(clasificacion)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public AtributoTecnico save(AtributoTecnico atributo) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(atributo)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}