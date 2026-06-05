package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.CategoriaDTO;

public interface CategoriaService {

    CategoriaDTO crear(CategoriaDTO request);

    CategoriaDTO actualizar(Integer id, CategoriaDTO request);

    CategoriaDTO obtenerPorId(Integer id);

    List<CategoriaDTO> listarTodas();

    void eliminar(Integer id);
}
