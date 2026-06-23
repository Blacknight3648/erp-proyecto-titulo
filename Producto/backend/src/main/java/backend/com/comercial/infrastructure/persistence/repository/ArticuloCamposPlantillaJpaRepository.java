package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.ArticuloCamposPlantillaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticuloCamposPlantillaJpaRepository extends JpaRepository<ArticuloCamposPlantillaJpaEntity, Long> {

    Optional<ArticuloCamposPlantillaJpaEntity> findByArticulo_IdArticulo(Integer idArticulo);

    void deleteByArticulo_IdArticulo(Integer idArticulo);
}
