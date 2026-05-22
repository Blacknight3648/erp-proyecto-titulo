package backend.com.shared.application.service;

import backend.com.shared.application.dto.DireccionRequest;
import backend.com.shared.application.dto.DireccionResponse;

import java.util.List;
import java.util.Optional;

public interface DireccionService {
    List<DireccionResponse> listarTodos();
    Optional<DireccionResponse> obtenerPorId(Long id);
    List<DireccionResponse> listarPorComuna(Long comunaId);
    DireccionResponse crear(DireccionRequest request);
    DireccionResponse actualizar(Long id, DireccionRequest request);
    void eliminar(Long id);
}
