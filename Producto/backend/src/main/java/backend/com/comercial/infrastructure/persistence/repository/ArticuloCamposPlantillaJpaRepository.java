package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.ArticuloCamposPlantillaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArticuloCamposPlantillaJpaRepository extends JpaRepository<ArticuloCamposPlantillaJpaEntity, Long> {

    Optional<ArticuloCamposPlantillaJpaEntity> findByArticulo_IdArticulo(Integer idArticulo);

    void deleteByArticulo_IdArticulo(Integer idArticulo);
}
