package backend.com.maestros.service;

import backend.com.maestros.dto.request.GramajeTelaRequestDTO;
import backend.com.maestros.dto.response.GramajeTelaResponseDTO;
import java.util.List;

public interface GramajeTelaService {
    GramajeTelaResponseDTO crear(GramajeTelaRequestDTO request);
    GramajeTelaResponseDTO actualizar(Integer id, GramajeTelaRequestDTO request);
    GramajeTelaResponseDTO obtenerPorId(Integer id);
    List<GramajeTelaResponseDTO> listarTodos();
    List<GramajeTelaResponseDTO> listarPorCategoriaVestuario(String categoria);
    void eliminar(Integer id);
}
