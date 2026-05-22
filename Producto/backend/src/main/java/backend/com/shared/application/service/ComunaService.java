package backend.com.shared.application.service;

import backend.com.shared.application.dto.ComunaRequest;
import backend.com.shared.application.dto.ComunaResponse;

import java.util.List;
import java.util.Optional;

public interface ComunaService {
    List<ComunaResponse> listarTodos();
    Optional<ComunaResponse> obtenerPorId(Long id);
    List<ComunaResponse> listarPorRegion(Long regionId);
    ComunaResponse crear(ComunaRequest request);
    ComunaResponse actualizar(Long id, ComunaRequest request);
    void eliminar(Long id);
}
