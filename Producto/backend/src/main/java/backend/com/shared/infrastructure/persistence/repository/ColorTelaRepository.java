package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.ColorTela;

import java.util.List;
import java.util.Optional;

public interface ColorTelaRepository {

    List<ColorTela> findAll();

    Optional<ColorTela> findById(Integer id);

    Optional<ColorTela> findByCodigoColor(String codigoColor);

    boolean existsByCodigoColor(String codigoColor);

    boolean existsById(Integer id);

    List<ColorTela> findByDescripcionColorContainingIgnoreCase(String descripcion);

    List<ColorTela> findByEsPantone(Boolean esPantone);

    ColorTela save(ColorTela color);

    void deleteById(Integer id);
}