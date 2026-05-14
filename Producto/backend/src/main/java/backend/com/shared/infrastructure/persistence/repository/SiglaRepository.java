package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.SiglaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiglaRepository extends JpaRepository<SiglaJpaEntity, Long> {

    boolean existsByDescripcionSigla(String descripcionSigla);

    boolean existsBySiglaAbreviatura(String siglaAbreviatura);

    Optional<SiglaJpaEntity> findBySiglaAbreviatura(String siglaAbreviatura);

    Optional<SiglaJpaEntity> findBySiglaAbreviaturaIgnoreCase(String siglaAbreviatura);

    List<SiglaJpaEntity> findByDescripcionSiglaContainingIgnoreCase(String descripcionSigla);

    Optional<SiglaJpaEntity> findByDescripcionSiglaIgnoreCase(String descripcionSigla);
}
