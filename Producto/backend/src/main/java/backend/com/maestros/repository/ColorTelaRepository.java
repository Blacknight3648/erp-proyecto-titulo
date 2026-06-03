package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.catalogo.ColorTela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColorTelaRepository extends JpaRepository<ColorTela, Integer> {

    Optional<ColorTela> findByCodigoColor(String codigoColor);

    boolean existsByCodigoColor(String codigoColor);

    List<ColorTela> findByDescripcionColorContainingIgnoreCase(String descripcion);

    List<ColorTela> findByEsPantone(Boolean esPantone);
}
