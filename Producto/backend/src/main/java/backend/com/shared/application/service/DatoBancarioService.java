package backend.com.shared.application.service;

import backend.com.shared.application.dto.DatoBancarioRequest;
import backend.com.shared.application.dto.DatoBancarioResponse;

import java.util.List;
import java.util.Optional;

public interface DatoBancarioService {
    List<DatoBancarioResponse> listarTodos();
    Optional<DatoBancarioResponse> obtenerPorId(Integer id);
    List<DatoBancarioResponse> listarPorBanco(Integer bancoId);
    DatoBancarioResponse crear(DatoBancarioRequest request);
    DatoBancarioResponse actualizar(Integer id, DatoBancarioRequest request);
    void eliminar(Integer id);
}
