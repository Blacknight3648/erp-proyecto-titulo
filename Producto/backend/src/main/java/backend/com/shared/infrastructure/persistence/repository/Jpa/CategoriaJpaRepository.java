package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.CategoriaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaJpaRepository extends JpaRepository<CategoriaJpaEntity, Integer> {

    Optional<CategoriaJpaEntity> findByCodigoCategoria(String codigoCategoria);

    Optional<CategoriaJpaEntity> findByNombreCategoria(String nombreCategoria);

    boolean existsByCodigoCategoria(String codigoCategoria);

    boolean existsByNombreCategoria(String nombreCategoria);
}