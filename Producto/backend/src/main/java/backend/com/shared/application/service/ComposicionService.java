package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.ComposicionDTO;

public interface ComposicionService {
    ComposicionDTO crear(ComposicionDTO request);

    ComposicionDTO actualizar(Integer id, ComposicionDTO request);

    ComposicionDTO obtenerPorId(Integer id);

    List<ComposicionDTO> listarTodas();

    List<ComposicionDTO> listarPorClasificacion(String clasificacion);

    void eliminar(Integer id);
}
