package backend.com.comercial.application.service;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;

import java.util.List;

public interface ArticuloCamposPlantillaService {

    ArticuloCamposPlantillaDTO crear(ArticuloCamposPlantillaDTO dto);

    List<ArticuloCamposPlantillaDTO> listarPorArticulo(Integer idArticulo);

    void eliminar(Long id);
}
