package backend.com.shared.application.service;

import backend.com.shared.application.dto.TipoAccesorioDTO;

import java.util.List;

public interface TipoAccesorioService {

    TipoAccesorioDTO crear(TipoAccesorioDTO request);

    TipoAccesorioDTO actualizar(Integer id, TipoAccesorioDTO request);

    TipoAccesorioDTO obtenerPorId(Integer id);

    List<TipoAccesorioDTO> listarTodos();

    void eliminar(Integer id);
}
