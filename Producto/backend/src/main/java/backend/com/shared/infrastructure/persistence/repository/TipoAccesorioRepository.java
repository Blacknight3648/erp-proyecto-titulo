package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.TipoAccesorio;

import java.util.List;
import java.util.Optional;

public interface TipoAccesorioRepository {

    List<TipoAccesorio> findAll();

    Optional<TipoAccesorio> findById(Integer id);

    boolean existsByCodigo(String codigo);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsById(Integer id);

    TipoAccesorio save(TipoAccesorio tipoAccesorio);

    void deleteById(Integer id);
}
