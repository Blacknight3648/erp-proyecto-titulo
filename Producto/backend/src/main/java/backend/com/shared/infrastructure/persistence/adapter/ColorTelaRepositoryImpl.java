package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.ColorTela;
import backend.com.shared.infrastructure.mapper.ColorTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.ColorTelaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.ColorTelaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ColorTelaRepositoryImpl implements ColorTelaRepository {

    private final ColorTelaJpaRepository jpaRepository;
    private final ColorTelaMapper mapper;

    @Override
    public List<ColorTela> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<ColorTela> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<ColorTela> findByCodigoColor(String codigoColor) {
        return jpaRepository.findByCodigoColor(codigoColor).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoColor(String codigoColor) {
        return jpaRepository.existsByCodigoColor(codigoColor);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public List<ColorTela> findByDescripcionColorContainingIgnoreCase(String descripcion) {
        return jpaRepository.findByDescripcionColorContainingIgnoreCase(descripcion)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<ColorTela> findByEsPantone(Boolean esPantone) {
        return jpaRepository.findByEsPantone(esPantone)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public ColorTela save(ColorTela color) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(color)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}