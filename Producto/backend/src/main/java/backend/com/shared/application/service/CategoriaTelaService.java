package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.CategoriaTelaDTO;

public interface CategoriaTelaService {

    CategoriaTelaDTO crear(CategoriaTelaDTO request);

    CategoriaTelaDTO actualizar(Integer id, CategoriaTelaDTO request);

    CategoriaTelaDTO obtenerPorId(Integer id);

    List<CategoriaTelaDTO> listarTodas();

    void eliminar(Integer id);
}
