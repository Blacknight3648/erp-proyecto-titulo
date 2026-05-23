package backend.com.shared.application.service;

import backend.com.shared.application.dto.TipoDireccionDTO;

import java.util.List;
import java.util.Optional;

public interface TipoDireccionService {
    List<TipoDireccionDTO> listarTodos();
    Optional<TipoDireccionDTO> obtenerPorId(Integer id);
    TipoDireccionDTO crear(TipoDireccionDTO dto);
    TipoDireccionDTO actualizar(Integer id, TipoDireccionDTO dto);
    void eliminar(Integer id);
}