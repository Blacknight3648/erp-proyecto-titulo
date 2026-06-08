package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.ArticuloCamposPlantillaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloCamposPlantillaJpaRepository extends JpaRepository<ArticuloCamposPlantillaJpaEntity, Long> {

    List<ArticuloCamposPlantillaJpaEntity> findByArticulo_IdArticulo(Integer idArticulo);

    boolean existsByArticulo_IdArticuloAndPlantilla_IdPlantilla(Integer idArticulo, Long idPlantilla);

    void deleteByArticulo_IdArticulo(Integer idArticulo);
}
