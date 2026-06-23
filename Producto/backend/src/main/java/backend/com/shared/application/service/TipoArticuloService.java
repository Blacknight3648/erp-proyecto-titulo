package backend.com.shared.application.service;

import backend.com.shared.application.dto.TipoArticuloDTO;

import java.util.List;

public interface TipoArticuloService {

    TipoArticuloDTO crear(TipoArticuloDTO request);

    TipoArticuloDTO actualizar(Integer id, TipoArticuloDTO request);

    TipoArticuloDTO obtenerPorId(Integer id);

    List<TipoArticuloDTO> listarTodos();

    void eliminar(Integer id);
}
