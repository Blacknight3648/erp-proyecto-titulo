package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.FamiliaTela;

import java.util.List;
import java.util.Optional;

public interface FamiliaTelaRepository {

    List<FamiliaTela> findAll();

    Optional<FamiliaTela> findById(Integer id);

    Optional<FamiliaTela> findByCodigoFamilia(String codigoFamilia);

    Optional<FamiliaTela> findByNombreFamiliaIgnoreCase(String nombreFamilia);

    boolean existsByCodigoFamilia(String codigoFamilia);

    boolean existsById(Integer id);

    FamiliaTela save(FamiliaTela familia);

    void deleteById(Integer id);
}