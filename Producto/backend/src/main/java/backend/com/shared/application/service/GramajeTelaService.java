package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.GramajeTelaDTO;

public interface GramajeTelaService {
    GramajeTelaDTO crear(GramajeTelaDTO request);

    GramajeTelaDTO actualizar(Integer id, GramajeTelaDTO request);

    GramajeTelaDTO obtenerPorId(Integer id);

    List<GramajeTelaDTO> listarTodos();

    List<GramajeTelaDTO> listarPorCategoriaVestuario(String categoria);

    void eliminar(Integer id);
}
