package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.GramajeTelaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GramajeTelaJpaRepository extends JpaRepository<GramajeTelaJpaEntity, Integer> {

    Optional<GramajeTelaJpaEntity> findByCodigoGramaje(String codigoGramaje);

    boolean existsByCodigoGramaje(String codigoGramaje);

    List<GramajeTelaJpaEntity> findByCategoriaVestuarioIgnoreCase(String categoriaVestuario);
}