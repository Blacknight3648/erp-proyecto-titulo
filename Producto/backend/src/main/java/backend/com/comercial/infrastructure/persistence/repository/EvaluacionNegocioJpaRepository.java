package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.EvaluacionNegocioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluacionNegocioJpaRepository extends JpaRepository<EvaluacionNegocioJpaEntity, Long> {
    List<EvaluacionNegocioJpaEntity> findByEstado(String estado);
    @org.springframework.data.jpa.repository.Query("SELECT MAX(CAST(e.numero AS long)) FROM EvaluacionNegocioJpaEntity e")
    java.util.Optional<Long> findMaxNumero();
}


