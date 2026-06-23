package backend.com.comercial.domain.repository;

import backend.com.comercial.domain.model.ArticuloCamposPlantilla;

import java.util.List;
import java.util.Optional;

public interface ArticuloCamposPlantillaRepository {

    List<ArticuloCamposPlantilla> findAll();

    Optional<ArticuloCamposPlantilla> findById(Long id);

    /** Configuración única del artículo (una fila por artículo). */
    Optional<ArticuloCamposPlantilla> findByArticuloId(Integer idArticulo);

    boolean existsById(Long id);

    ArticuloCamposPlantilla save(ArticuloCamposPlantilla modelo);

    void deleteById(Long id);

    void deleteByArticuloId(Integer idArticulo);
}
