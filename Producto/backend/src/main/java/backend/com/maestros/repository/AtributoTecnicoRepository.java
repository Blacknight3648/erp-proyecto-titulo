package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.catalogo.AtributoTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtributoTecnicoRepository extends JpaRepository<AtributoTecnico, Integer> {

    Optional<AtributoTecnico> findByCodigoAtributo(String codigoAtributo);

    boolean existsByCodigoAtributo(String codigoAtributo);

    /** Filtrar atributos por clasificación (ej: "RESISTENCIA", "ACABADO", "HIGIENE"). */
    List<AtributoTecnico> findByClasificacionIgnoreCase(String clasificacion);
}
