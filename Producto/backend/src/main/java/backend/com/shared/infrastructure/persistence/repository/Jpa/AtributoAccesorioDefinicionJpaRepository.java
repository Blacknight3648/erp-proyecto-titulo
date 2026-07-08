package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.AtributoAccesorioDefinicionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtributoAccesorioDefinicionJpaRepository
        extends JpaRepository<AtributoAccesorioDefinicionJpaEntity, Integer> {

    List<AtributoAccesorioDefinicionJpaEntity> findByTipoAccesorio_IdTipoAccesorioOrderByOrdenAsc(
            Integer idTipoAccesorio);
}
