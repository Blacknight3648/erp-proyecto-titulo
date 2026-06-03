package backend.com.maestros.service;

import backend.com.maestros.dto.request.AtributoTecnicoRequestDTO;
import backend.com.maestros.dto.response.AtributoTecnicoResponseDTO;
import java.util.List;

public interface AtributoTecnicoService {
    AtributoTecnicoResponseDTO crear(AtributoTecnicoRequestDTO request);
    AtributoTecnicoResponseDTO actualizar(Integer id, AtributoTecnicoRequestDTO request);
    AtributoTecnicoResponseDTO obtenerPorId(Integer id);
    List<AtributoTecnicoResponseDTO> listarTodos();
    List<AtributoTecnicoResponseDTO> listarPorClasificacion(String clasificacion);
    void eliminar(Integer id);
}
