package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.PrecioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PrecioJpaRepository extends JpaRepository<PrecioJpaEntity, Integer> {

    List<PrecioJpaEntity> findByArticulo_IdArticulo(Integer idArticulo);

    Optional<PrecioJpaEntity> findByArticulo_IdArticuloAndTipoPrecio(Integer idArticulo, String tipoPrecio);

    List<PrecioJpaEntity> findByArticulo_IdArticuloAndMoneda_IdMoneda(Integer idArticulo, Integer idMoneda);

    void deleteByArticulo_IdArticulo(Integer idArticulo);
}