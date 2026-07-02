package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.GramajeTelaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GramajeTelaJpaRepository extends JpaRepository<GramajeTelaJpaEntity, Integer> {

    Optional<GramajeTelaJpaEntity> findByCodigoGramaje(String codigoGramaje);

    boolean existsByCodigoGramaje(String codigoGramaje);

    List<GramajeTelaJpaEntity> findByCategoriaVestuarioIgnoreCase(String categoriaVestuario);
}