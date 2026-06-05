package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.GramajeTela;

import java.util.List;
import java.util.Optional;

public interface GramajeTelaRepository {

    List<GramajeTela> findAll();

    Optional<GramajeTela> findById(Integer id);

    Optional<GramajeTela> findByCodigoGramaje(String codigoGramaje);

    boolean existsByCodigoGramaje(String codigoGramaje);

    boolean existsById(Integer id);

    List<GramajeTela> findByCategoriaVestuarioIgnoreCase(String categoriaVestuario);

    GramajeTela save(GramajeTela gramaje);

    void deleteById(Integer id);
}