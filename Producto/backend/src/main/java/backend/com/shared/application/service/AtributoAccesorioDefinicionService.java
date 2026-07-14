package backend.com.shared.application.service;

import backend.com.shared.application.dto.AtributoAccesorioDefinicionDTO;

import java.util.List;

public interface AtributoAccesorioDefinicionService {

    AtributoAccesorioDefinicionDTO crear(AtributoAccesorioDefinicionDTO request);

    AtributoAccesorioDefinicionDTO actualizar(Integer id, AtributoAccesorioDefinicionDTO request);

    AtributoAccesorioDefinicionDTO obtenerPorId(Integer id);

    List<AtributoAccesorioDefinicionDTO> listarTodos();

    List<AtributoAccesorioDefinicionDTO> listarPorTipoAccesorio(Integer idTipoAccesorio);

    void eliminar(Integer id);
}
