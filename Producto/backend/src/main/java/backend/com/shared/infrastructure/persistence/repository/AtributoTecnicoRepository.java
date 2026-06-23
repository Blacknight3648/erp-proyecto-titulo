package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.AtributoTecnico;

import java.util.List;
import java.util.Optional;

public interface AtributoTecnicoRepository {

    List<AtributoTecnico> findAll();

    Optional<AtributoTecnico> findById(Integer id);

    Optional<AtributoTecnico> findByCodigoAtributo(String codigoAtributo);

    boolean existsByCodigoAtributo(String codigoAtributo);

    boolean existsById(Integer id);

    List<AtributoTecnico> findByClasificacionIgnoreCase(String clasificacion);

    AtributoTecnico save(AtributoTecnico atributo);

    void deleteById(Integer id);
}