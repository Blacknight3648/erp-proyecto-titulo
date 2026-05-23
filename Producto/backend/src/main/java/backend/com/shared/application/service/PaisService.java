package backend.com.shared.application.service;

import backend.com.shared.application.dto.PaisDTO;

import java.util.List;
import java.util.Optional;

public interface PaisService {
    List<PaisDTO> listarTodos();
    Optional<PaisDTO> obtenerPorId(Integer id);
    PaisDTO crear(PaisDTO dto);
    PaisDTO actualizar(Integer id, PaisDTO dto);
    void eliminar(Integer id);
}