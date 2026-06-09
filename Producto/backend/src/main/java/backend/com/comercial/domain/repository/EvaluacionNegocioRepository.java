package backend.com.comercial.domain.repository;

import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.shared.valueobjects.DocumentNumber;

import java.util.Optional;

public interface EvaluacionNegocioRepository {
    EvaluacionNegocio save(EvaluacionNegocio evaluacionNegocio);

    Optional<EvaluacionNegocio> findById(Long id);

    Optional<EvaluacionNegocio> findByNumero(DocumentNumber numero);

    java.util.List<EvaluacionNegocio> findAll();

    /** IDs de costeos ya vinculados a algún ítem de cualquier EVN. */
    java.util.List<Long> findLinkedCosteoIds();
}
