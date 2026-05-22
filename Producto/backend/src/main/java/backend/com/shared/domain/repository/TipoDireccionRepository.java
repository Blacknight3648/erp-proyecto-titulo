package backend.com.shared.domain.repository;

import backend.com.shared.domain.model.TipoDireccion;

import java.util.List;
import java.util.Optional;

public interface TipoDireccionRepository {
    List<TipoDireccion> findAll();
    Optional<TipoDireccion> findById(Integer id);
    TipoDireccion save(TipoDireccion tipoDireccion);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
