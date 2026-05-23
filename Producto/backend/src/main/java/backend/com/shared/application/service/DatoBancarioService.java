package backend.com.shared.application.service;

import backend.com.shared.application.dto.DatoBancarioDTO;

import java.util.List;
import java.util.Optional;

public interface DatoBancarioService {
    List<DatoBancarioDTO> listarTodos();
    Optional<DatoBancarioDTO> obtenerPorId(Integer id);
    List<DatoBancarioDTO> listarPorBanco(Integer bancoId);
    DatoBancarioDTO crear(DatoBancarioDTO dto);
    DatoBancarioDTO actualizar(Integer id, DatoBancarioDTO dto);
    void eliminar(Integer id);
}