package backend.com.shared.application.service;

import backend.com.shared.application.dto.DireccionDTO;

import java.util.List;
import java.util.Optional;

public interface DireccionService {
    List<DireccionDTO> listarTodos();
    Optional<DireccionDTO> obtenerPorId(Long id);
    List<DireccionDTO> listarPorComuna(Long comunaId);
    DireccionDTO crear(DireccionDTO dto);
    DireccionDTO actualizar(Long id, DireccionDTO dto);
    void eliminar(Long id);
}