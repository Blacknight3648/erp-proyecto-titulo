package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.catalogo.ClasificacionTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClasificacionTecnicaRepository extends JpaRepository<ClasificacionTecnica, Integer> {

    Optional<ClasificacionTecnica> findByNombreClasificacionIgnoreCase(String nombreClasificacion);

    boolean existsByNombreClasificacion(String nombreClasificacion);
}
