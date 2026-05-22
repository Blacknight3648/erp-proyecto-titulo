package backend.com.shared.application.service;

import backend.com.shared.application.dto.PaisRequest;
import backend.com.shared.application.dto.PaisResponse;

import java.util.List;
import java.util.Optional;

public interface PaisService {
    List<PaisResponse> listarTodos();
    Optional<PaisResponse> obtenerPorId(Integer id);
    PaisResponse crear(PaisRequest request);
    PaisResponse actualizar(Integer id, PaisRequest request);
    void eliminar(Integer id);
}
