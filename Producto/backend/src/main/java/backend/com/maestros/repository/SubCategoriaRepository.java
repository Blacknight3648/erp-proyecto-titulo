package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Integer> {

    Optional<SubCategoria> findByCodigoSubcategoria(String codigoSubcategoria);

    boolean existsByCodigoSubcategoria(String codigoSubcategoria);

    /** Todas las subcategorías que pertenecen a una categoría padre. */
    List<SubCategoria> findByCategoria_IdCategoria(Integer idCategoria);
}
