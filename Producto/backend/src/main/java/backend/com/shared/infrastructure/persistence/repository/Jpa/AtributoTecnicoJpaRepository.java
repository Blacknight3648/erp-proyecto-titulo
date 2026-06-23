package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.AtributoTecnicoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtributoTecnicoJpaRepository extends JpaRepository<AtributoTecnicoJpaEntity, Integer> {

    Optional<AtributoTecnicoJpaEntity> findByCodigoAtributo(String codigoAtributo);

    boolean existsByCodigoAtributo(String codigoAtributo);

    List<AtributoTecnicoJpaEntity> findByClasificacionIgnoreCase(String clasificacion);
}