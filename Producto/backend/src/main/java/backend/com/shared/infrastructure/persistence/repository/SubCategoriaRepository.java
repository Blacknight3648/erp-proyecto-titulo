package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.SubCategoria;

import java.util.List;
import java.util.Optional;

public interface SubCategoriaRepository {

    List<SubCategoria> findAll();

    Optional<SubCategoria> findById(Integer id);

    Optional<SubCategoria> findByCodigoSubcategoria(String codigoSubcategoria);

    boolean existsByCodigoSubcategoria(String codigoSubcategoria);

    boolean existsById(Integer id);

    List<SubCategoria> findByCategoriaId(Integer idCategoria);

    SubCategoria save(SubCategoria subCategoria);

    void deleteById(Integer id);
}