package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaisJpaRepository extends JpaRepository<Pais, Integer> {
    Optional<Pais> findByNombrePais(String nombrePais);
}
