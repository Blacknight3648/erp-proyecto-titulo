package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Pais;

import java.util.List;
import java.util.Optional;

public interface PaisRepository {
    List<Pais> findAll();

    Optional<Pais> findById(Integer id);

    Optional<Pais> findByNombrePais(String nombrePais);

    Pais save(Pais pais);

    void deleteById(Integer id);

    boolean existsById(Integer id);
}
