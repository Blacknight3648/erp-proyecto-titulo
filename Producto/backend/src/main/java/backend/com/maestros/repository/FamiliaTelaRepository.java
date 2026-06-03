package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.catalogo.FamiliaTela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamiliaTelaRepository extends JpaRepository<FamiliaTela, Integer> {

    Optional<FamiliaTela> findByCodigoFamilia(String codigoFamilia);

    Optional<FamiliaTela> findByNombreFamiliaIgnoreCase(String nombreFamilia);

    boolean existsByCodigoFamilia(String codigoFamilia);
}
