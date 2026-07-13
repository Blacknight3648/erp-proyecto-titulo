package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.ModeloJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ModeloJpaRepository extends JpaRepository<ModeloJpaEntity, Integer> {

    Optional<ModeloJpaEntity> findByCodigoModelo(String codigoModelo);

    Optional<ModeloJpaEntity> findByNombreModelo(String nombreModelo);

    boolean existsByCodigoModelo(String codigoModelo);

    boolean existsByNombreModelo(String nombreModelo);
}
