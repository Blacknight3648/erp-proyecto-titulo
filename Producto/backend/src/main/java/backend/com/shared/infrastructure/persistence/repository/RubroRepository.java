package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Rubro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RubroRepository extends JpaRepository<Rubro, Long> {

    List<Rubro> findAllByOrderByNombreRubroAsc();

}
