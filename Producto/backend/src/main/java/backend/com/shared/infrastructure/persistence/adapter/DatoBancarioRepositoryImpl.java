package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.DatoBancario;
import backend.com.shared.domain.repository.DatoBancarioRepository;
import backend.com.shared.infrastructure.mapper.DatoBancarioMapper;
import backend.com.shared.infrastructure.persistence.repository.DatoBancarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DatoBancarioRepositoryImpl implements DatoBancarioRepository {

    private final DatoBancarioJpaRepository jpaRepository;
    private final DatoBancarioMapper mapper;

    @Override
    public List<DatoBancario> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<DatoBancario> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<DatoBancario> findByBancoId(Integer bancoId) {
        return jpaRepository.findByBanco_BancoId(bancoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public DatoBancario save(DatoBancario datoBancario) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(datoBancario)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }
}
