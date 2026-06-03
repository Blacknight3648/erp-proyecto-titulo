package backend.com.maestros.service;

import backend.com.maestros.dto.request.ComposicionRequestDTO;
import backend.com.maestros.dto.response.ComposicionResponseDTO;
import java.util.List;

public interface ComposicionService {
    ComposicionResponseDTO crear(ComposicionRequestDTO request);
    ComposicionResponseDTO actualizar(Integer id, ComposicionRequestDTO request);
    ComposicionResponseDTO obtenerPorId(Integer id);
    List<ComposicionResponseDTO> listarTodas();
    List<ComposicionResponseDTO> listarPorClasificacion(String clasificacion);
    void eliminar(Integer id);
}
