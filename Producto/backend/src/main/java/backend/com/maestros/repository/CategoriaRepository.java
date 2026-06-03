package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByCodigoCategoria(String codigoCategoria);

    Optional<Categoria> findByNombreCategoria(String nombreCategoria);

    boolean existsByCodigoCategoria(String codigoCategoria);

    boolean existsByNombreCategoria(String nombreCategoria);
}
