package backend.com.comercial.domain.repository;

import backend.com.comercial.domain.model.DescripcionPlantilla;

import java.util.List;
import java.util.Optional;

public interface DescripcionPlantillaRepository {

    Optional<DescripcionPlantilla> findById(Long id);

    boolean existsById(Long id);

    List<DescripcionPlantilla> findByIdSCOS(Long idSCOS);

    DescripcionPlantilla save(DescripcionPlantilla descripcion);

    void deleteById(Long id);

    void deleteByIdSCOS(Long idSCOS);
}
