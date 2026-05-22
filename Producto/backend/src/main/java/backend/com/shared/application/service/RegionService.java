package backend.com.shared.application.service;

import backend.com.shared.application.dto.RegionRequest;
import backend.com.shared.application.dto.RegionResponse;

import java.util.List;
import java.util.Optional;

public interface RegionService {
    List<RegionResponse> listarTodos();
    Optional<RegionResponse> obtenerPorId(Long id);
    List<RegionResponse> listarPorPais(Integer paisId);
    RegionResponse crear(RegionRequest request);
    RegionResponse actualizar(Long id, RegionRequest request);
    void eliminar(Long id);
}
