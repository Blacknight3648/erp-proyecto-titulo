package backend.com.comercial.application.service;

import backend.com.comercial.application.dto.CamposPlantillaDTO;

import java.util.List;

public interface CamposPlantillaService {

    CamposPlantillaDTO crear(CamposPlantillaDTO dto);

    CamposPlantillaDTO actualizar(Long id, CamposPlantillaDTO dto);

    CamposPlantillaDTO obtenerPorId(Long id);

    List<CamposPlantillaDTO> listarTodas();

    void eliminar(Long id);
}
