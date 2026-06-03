package backend.com.maestros.service;

import backend.com.maestros.dto.request.ColorTelaRequestDTO;
import backend.com.maestros.dto.response.ColorTelaResponseDTO;
import java.util.List;

public interface ColorTelaService {
    ColorTelaResponseDTO crear(ColorTelaRequestDTO request);
    ColorTelaResponseDTO actualizar(Integer id, ColorTelaRequestDTO request);
    ColorTelaResponseDTO obtenerPorId(Integer id);
    List<ColorTelaResponseDTO> listarTodos();
    List<ColorTelaResponseDTO> buscarPorDescripcion(String descripcion);
    List<ColorTelaResponseDTO> listarPantone();
    void eliminar(Integer id);
}
