package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {

    List<Categoria> findAll();

    Optional<Categoria> findById(Integer id);

    Optional<Categoria> findByCodigoCategoria(String codigoCategoria);

    Optional<Categoria> findByNombreCategoria(String nombreCategoria);

    boolean existsByCodigoCategoria(String codigoCategoria);

    boolean existsByNombreCategoria(String nombreCategoria);

    boolean existsById(Integer id);

    Categoria save(Categoria categoria);

    void deleteById(Integer id);
}