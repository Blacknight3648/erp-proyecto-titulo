package backend.com.comercial.application.service;

import backend.com.comercial.application.dto.DescripcionPlantillaDTO;

import java.util.List;

public interface DescripcionPlantillaService {

    DescripcionPlantillaDTO crear(DescripcionPlantillaDTO dto);

    DescripcionPlantillaDTO actualizar(Long id, DescripcionPlantillaDTO dto);

    DescripcionPlantillaDTO obtenerPorId(Long id);

    List<DescripcionPlantillaDTO> listarPorSCOS(Long idSCOS);

    void eliminar(Long id);
}
