package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository {
    List<Producto> findAll();

    Optional<Producto> findById(Long id);

    Optional<Producto> findByCodigo(String codigo);

    Producto save(Producto producto);

    void deleteById(Long id);
}
