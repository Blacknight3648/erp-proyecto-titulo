package backend.com.shared.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import backend.com.shared.domain.model.Rubro;

public interface RubroRepository {

    List<Rubro> findAll();

    Optional<Rubro> findById(Long id);

    Optional<Rubro> findByNombreRubro(String nombreRubro);

    Rubro save(Rubro rubro);

    void deleteById(Long id);

    List<Rubro> findAllByOrderByNombreRubroAsc();
}
