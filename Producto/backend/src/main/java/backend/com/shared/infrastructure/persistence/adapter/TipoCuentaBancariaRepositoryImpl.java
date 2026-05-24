package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.TipoCuentaBancaria;
import backend.com.shared.infrastructure.persistence.repository.TipoCuentaBancariaRepository;
import backend.com.shared.infrastructure.mapper.TipoCuentaBancariaMapper;
import backend.com.shared.infrastructure.persistence.repository.Jpa.TipoCuentaBancariaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TipoCuentaBancariaRepositoryImpl implements TipoCuentaBancariaRepository {

    private final TipoCuentaBancariaJpaRepository jpaRepository;
    private final TipoCuentaBancariaMapper mapper;

    @Override
    public List<TipoCuentaBancaria> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<TipoCuentaBancaria> findById(Integer id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public TipoCuentaBancaria save(TipoCuentaBancaria tipoCuenta) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(tipoCuenta)));
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
