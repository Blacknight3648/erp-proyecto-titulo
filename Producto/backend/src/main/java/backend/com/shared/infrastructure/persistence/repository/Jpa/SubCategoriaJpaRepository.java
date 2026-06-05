package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.SubCategoriaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoriaJpaRepository extends JpaRepository<SubCategoriaJpaEntity, Integer> {

    Optional<SubCategoriaJpaEntity> findByCodigoSubcategoria(String codigoSubcategoria);

    boolean existsByCodigoSubcategoria(String codigoSubcategoria);

    List<SubCategoriaJpaEntity> findByCategoria_IdCategoria(Integer idCategoria);
}