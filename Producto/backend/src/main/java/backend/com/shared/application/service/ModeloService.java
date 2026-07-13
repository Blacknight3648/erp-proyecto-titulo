package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.ModeloDTO;

public interface ModeloService {

    ModeloDTO crear(ModeloDTO request);

    ModeloDTO actualizar(Integer id, ModeloDTO request);

    ModeloDTO obtenerPorId(Integer id);

    List<ModeloDTO> listarTodos();

    void eliminar(Integer id);
}
