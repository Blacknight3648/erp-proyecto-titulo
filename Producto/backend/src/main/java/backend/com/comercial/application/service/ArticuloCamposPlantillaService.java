package backend.com.comercial.application.service;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;

import java.util.List;

public interface ArticuloCamposPlantillaService {
    ArticuloCamposPlantillaDTO crear(ArticuloCamposPlantillaDTO dto);

    List<ArticuloCamposPlantillaDTO> listarPorNombreArticulo(String nombreArticulo);

    List<ArticuloCamposPlantillaDTO> listarPorArticulo(Integer idArticulo);

    List<ArticuloCamposPlantillaDTO> guardarCampos(List<ArticuloCamposPlantillaDTO> requeList);

    void eliminar(Long id);
}
