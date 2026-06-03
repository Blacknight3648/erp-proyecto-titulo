package backend.com.maestros.service;

import backend.com.maestros.dto.request.SubCategoriaRequestDTO;
import backend.com.maestros.dto.response.SubCategoriaResponseDTO;

import java.util.List;

public interface SubCategoriaService {

    SubCategoriaResponseDTO crear(SubCategoriaRequestDTO request);

    SubCategoriaResponseDTO actualizar(Integer id, SubCategoriaRequestDTO request);

    SubCategoriaResponseDTO obtenerPorId(Integer id);

    List<SubCategoriaResponseDTO> listarTodas();

    List<SubCategoriaResponseDTO> listarPorCategoria(Integer idCategoria);

    void eliminar(Integer id);
}
