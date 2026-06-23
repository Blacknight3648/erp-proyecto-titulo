package backend.com.comercial.domain.repository;

import backend.com.comercial.domain.model.ArticuloCamposPlantilla;

import java.util.List;
import java.util.Optional;

public interface ArticuloCamposPlantillaRepository {

    List<ArticuloCamposPlantilla> findAll();

    Optional<ArticuloCamposPlantilla> findById(Long id);

    List<ArticuloCamposPlantilla> findByArticuloId(Integer idArticulo);

    boolean existsByArticuloIdAndPlantillaId(Integer idArticulo, Long idPlantilla);

    boolean existsById(Long id);

    ArticuloCamposPlantilla save(ArticuloCamposPlantilla modelo);

    void deleteById(Long id);

    void deleteByArticuloId(Integer idArticulo);
}
