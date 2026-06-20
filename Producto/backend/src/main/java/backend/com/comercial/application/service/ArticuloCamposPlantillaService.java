package backend.com.comercial.application.service;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;

import java.util.Optional;

public interface ArticuloCamposPlantillaService {

    /** Crea o actualiza (upsert) la única fila de campos del artículo. */
    ArticuloCamposPlantillaDTO guardar(ArticuloCamposPlantillaDTO dto);

    /** Configuración de campos del artículo (vacío si no tiene). */
    Optional<ArticuloCamposPlantillaDTO> obtenerPorArticulo(Integer idArticulo);

    void eliminarPorArticulo(Integer idArticulo);
}
