package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.ColorTelaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ColorTelaJpaRepository extends JpaRepository<ColorTelaJpaEntity, Integer> {

    Optional<ColorTelaJpaEntity> findByCodigoColor(String codigoColor);

    boolean existsByCodigoColor(String codigoColor);

    List<ColorTelaJpaEntity> findByDescripcionColorContainingIgnoreCase(String descripcion);

    List<ColorTelaJpaEntity> findByEsPantone(Boolean esPantone);
}