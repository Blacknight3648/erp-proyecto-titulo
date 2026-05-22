package backend.com.shared.application.service;

import backend.com.shared.application.dto.TipoDireccionRequest;
import backend.com.shared.application.dto.TipoDireccionResponse;

import java.util.List;
import java.util.Optional;

public interface TipoDireccionService {
    List<TipoDireccionResponse> listarTodos();
    Optional<TipoDireccionResponse> obtenerPorId(Integer id);
    TipoDireccionResponse crear(TipoDireccionRequest request);
    TipoDireccionResponse actualizar(Integer id, TipoDireccionRequest request);
    void eliminar(Integer id);
}
