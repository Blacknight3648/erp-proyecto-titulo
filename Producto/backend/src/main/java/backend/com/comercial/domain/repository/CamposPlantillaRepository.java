package backend.com.comercial.domain.repository;

import backend.com.comercial.domain.model.CamposPlantilla;

import java.util.List;
import java.util.Optional;

public interface CamposPlantillaRepository {

    List<CamposPlantilla> findAll();

    Optional<CamposPlantilla> findById(Long id);

    Optional<CamposPlantilla> findByNombreCampo(String nombreCampo);

    boolean existsByNombreCampo(String nombreCampo);

    boolean existsById(Long id);

    CamposPlantilla save(CamposPlantilla plantilla);

    void deleteById(Long id);
}
