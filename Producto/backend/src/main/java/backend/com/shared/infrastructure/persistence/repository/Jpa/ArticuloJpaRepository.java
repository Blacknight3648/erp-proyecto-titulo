package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.domain.enums.TipoArticulo;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticuloJpaRepository extends JpaRepository<ArticuloJpaEntity, Integer> {

    Optional<ArticuloJpaEntity> findByCodigoArticulo(String codigoArticulo);

    boolean existsByCodigoArticulo(String codigoArticulo);

    List<ArticuloJpaEntity> findByActivoTrue();

    List<ArticuloJpaEntity> findByTipoArticulo(TipoArticulo tipoArticulo);

    List<ArticuloJpaEntity> findByNombreArticuloContainingIgnoreCase(String nombre);
}