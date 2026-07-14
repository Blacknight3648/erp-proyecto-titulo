package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Modelo;
import backend.com.shared.infrastructure.mapper.ModeloMapper;
import backend.com.shared.infrastructure.persistence.repository.ModeloRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.ModeloJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ModeloRepositoryImpl implements ModeloRepository {

    private final ModeloJpaRepository jpaRepository;
    private final ModeloMapper mapper;

    @Override
    public List<Modelo> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Modelo> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Modelo> findByCodigoModelo(String codigoModelo) {
        return jpaRepository.findByCodigoModelo(codigoModelo).map(mapper::toDomain);
    }

    @Override
    public Optional<Modelo> findByNombreModelo(String nombreModelo) {
        return jpaRepository.findByNombreModelo(nombreModelo).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoModelo(String codigoModelo) {
        return jpaRepository.existsByCodigoModelo(codigoModelo);
    }

    @Override
    public boolean existsByNombreModelo(String nombreModelo) {
        return jpaRepository.existsByNombreModelo(nombreModelo);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public Modelo save(Modelo modelo) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(modelo)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}
