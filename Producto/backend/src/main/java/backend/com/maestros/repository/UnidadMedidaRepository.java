package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Integer> {

    Optional<UnidadMedida> findByNombreUnidad(String nombreUnidad);

    Optional<UnidadMedida> findByAbreviatura(String abreviatura);

    boolean existsByAbreviatura(String abreviatura);
}
