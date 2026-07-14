package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.AtributoAccesorioDefinicion;

import java.util.List;
import java.util.Optional;

public interface AtributoAccesorioDefinicionRepository {

    List<AtributoAccesorioDefinicion> findAll();

    Optional<AtributoAccesorioDefinicion> findById(Integer id);

    List<AtributoAccesorioDefinicion> findByTipoAccesorioIdOrderByOrdenAsc(Integer idTipoAccesorio);

    boolean existsById(Integer id);

    AtributoAccesorioDefinicion save(AtributoAccesorioDefinicion definicion);

    void deleteById(Integer id);
}
