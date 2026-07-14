package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.CategoriaTelaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriaTelaJpaRepository extends JpaRepository<CategoriaTelaJpaEntity, Integer> {

    Optional<CategoriaTelaJpaEntity> findByCodigoCategoriaTela(String codigoCategoriaTela);

    Optional<CategoriaTelaJpaEntity> findByNombreCategoriaTela(String nombreCategoriaTela);

    boolean existsByCodigoCategoriaTela(String codigoCategoriaTela);

    boolean existsByNombreCategoriaTela(String nombreCategoriaTela);
}
