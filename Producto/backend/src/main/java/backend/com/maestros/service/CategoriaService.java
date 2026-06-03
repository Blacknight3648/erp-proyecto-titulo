package backend.com.maestros.service;

import backend.com.maestros.dto.request.CategoriaRequestDTO;
import backend.com.maestros.dto.response.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {

    CategoriaResponseDTO crear(CategoriaRequestDTO request);

    CategoriaResponseDTO actualizar(Integer id, CategoriaRequestDTO request);

    CategoriaResponseDTO obtenerPorId(Integer id);

    List<CategoriaResponseDTO> listarTodas();

    void eliminar(Integer id);
}
