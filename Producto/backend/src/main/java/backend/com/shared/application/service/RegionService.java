package backend.com.shared.application.service;

import backend.com.shared.application.dto.RegionDTO;

import java.util.List;
import java.util.Optional;

public interface RegionService {
    List<RegionDTO> listarTodos();
    Optional<RegionDTO> obtenerPorId(Long id);
    List<RegionDTO> listarPorPais(Integer paisId);
    RegionDTO crear(RegionDTO dto);
    RegionDTO actualizar(Long id, RegionDTO dto);
    void eliminar(Long id);
}