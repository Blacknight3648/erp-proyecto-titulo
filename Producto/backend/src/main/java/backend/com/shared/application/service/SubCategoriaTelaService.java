package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.SubCategoriaTelaDTO;

public interface SubCategoriaTelaService {

    SubCategoriaTelaDTO crear(SubCategoriaTelaDTO request);

    SubCategoriaTelaDTO actualizar(Integer id, SubCategoriaTelaDTO request);

    SubCategoriaTelaDTO obtenerPorId(Integer id);

    List<SubCategoriaTelaDTO> listarTodas();

    List<SubCategoriaTelaDTO> listarPorCategoriaTela(Integer idCategoriaTela);

    void eliminar(Integer id);
}
