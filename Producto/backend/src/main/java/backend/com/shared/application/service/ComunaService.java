package backend.com.shared.application.service;

import backend.com.shared.application.dto.ComunaDTO;

import java.util.List;
import java.util.Optional;

public interface ComunaService {
    List<ComunaDTO> listarTodos();
    Optional<ComunaDTO> obtenerPorId(Long id);
    List<ComunaDTO> listarPorRegion(Long regionId);
    ComunaDTO crear(ComunaDTO dto);
    ComunaDTO actualizar(Long id, ComunaDTO dto);
    void eliminar(Long id);
}