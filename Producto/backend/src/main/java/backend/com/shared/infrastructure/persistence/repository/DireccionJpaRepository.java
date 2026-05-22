package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionJpaRepository extends JpaRepository<Direccion, Long> {
    List<Direccion> findByComuna_ComunaId(Long comunaId);
}
