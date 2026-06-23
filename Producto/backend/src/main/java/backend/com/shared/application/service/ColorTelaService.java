package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.ColorTelaDTO;

public interface ColorTelaService {
    ColorTelaDTO crear(ColorTelaDTO request);

    ColorTelaDTO actualizar(Integer id, ColorTelaDTO request);

    ColorTelaDTO obtenerPorId(Integer id);

    List<ColorTelaDTO> listarTodos();

    List<ColorTelaDTO> buscarPorDescripcion(String descripcion);

    List<ColorTelaDTO> listarPantone();

    void eliminar(Integer id);
}
