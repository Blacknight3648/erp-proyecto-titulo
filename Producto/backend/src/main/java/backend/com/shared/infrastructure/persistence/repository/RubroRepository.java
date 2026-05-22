package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Rubro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RubroRepository extends JpaRepository<Rubro, Long> {

    List<Rubro> findAllByOrderByNombreRubroAsc();

    Optional<Rubro> findByCodigoSii(String codigoSii);

    Optional<Rubro> findByNombreRubro(String nombreRubro);

}
