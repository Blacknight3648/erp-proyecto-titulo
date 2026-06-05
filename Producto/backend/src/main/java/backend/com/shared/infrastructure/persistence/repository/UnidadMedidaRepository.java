package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.UnidadMedida;

import java.util.List;
import java.util.Optional;

public interface UnidadMedidaRepository {

    List<UnidadMedida> findAll();

    Optional<UnidadMedida> findById(Integer id);

    Optional<UnidadMedida> findByNombreUnidad(String nombreUnidad);

    Optional<UnidadMedida> findByAbreviatura(String abreviatura);

    boolean existsByAbreviatura(String abreviatura);

    boolean existsById(Integer id);

    UnidadMedida save(UnidadMedida unidad);

    void deleteById(Integer id);
}