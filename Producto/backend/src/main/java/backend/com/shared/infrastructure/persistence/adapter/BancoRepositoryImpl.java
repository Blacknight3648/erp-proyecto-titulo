package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Banco;
import backend.com.shared.infrastructure.persistence.repository.BancoRepository;
import backend.com.shared.infrastructure.mapper.BancoMapper;
import backend.com.shared.infrastructure.persistence.repository.Jpa.BancoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BancoRepositoryImpl implements BancoRepository {

    private final BancoJpaRepository jpaRepository;
    private final BancoMapper mapper;

    @Override
    public List<Banco> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Banco> findById(Integer id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Banco> findByCodigoBanco(String codigoBanco) {
        return jpaRepository.findByCodigoBanco(codigoBanco).map(mapper::toDomain);
    }

    @Override
    public Banco save(Banco banco) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(banco)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null)
            jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }
}
