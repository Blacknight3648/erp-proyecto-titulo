package backend.com.maestros.service;

import backend.com.maestros.dto.request.ClasificacionTecnicaRequestDTO;
import backend.com.maestros.dto.response.ClasificacionTecnicaResponseDTO;
import java.util.List;

public interface ClasificacionTecnicaService {
    ClasificacionTecnicaResponseDTO crear(ClasificacionTecnicaRequestDTO request);
    ClasificacionTecnicaResponseDTO actualizar(Integer id, ClasificacionTecnicaRequestDTO request);
    ClasificacionTecnicaResponseDTO obtenerPorId(Integer id);
    List<ClasificacionTecnicaResponseDTO> listarTodas();
    void eliminar(Integer id);
}
