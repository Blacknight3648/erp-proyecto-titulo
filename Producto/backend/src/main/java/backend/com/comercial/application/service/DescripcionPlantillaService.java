package backend.com.comercial.application.service;

import backend.com.comercial.application.dto.DescripcionPlantillaDTO;

import java.util.List;

public interface DescripcionPlantillaService {

    DescripcionPlantillaDTO crear(DescripcionPlantillaDTO dto);

    /** Guarda en lote, de forma atómica, todas las descripciones de una SCOS. */
    List<DescripcionPlantillaDTO> guardarMultiples(List<DescripcionPlantillaDTO> dtos);

    DescripcionPlantillaDTO actualizar(Long id, DescripcionPlantillaDTO dto);

    DescripcionPlantillaDTO obtenerPorId(Long id);

    List<DescripcionPlantillaDTO> listarPorSCOS(Long idSCOS);

    void eliminar(Long id);
}
