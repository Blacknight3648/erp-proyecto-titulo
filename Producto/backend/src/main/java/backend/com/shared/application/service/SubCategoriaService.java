package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.SubCategoriaDTO;

public interface SubCategoriaService {

    SubCategoriaDTO crear(SubCategoriaDTO request);

    SubCategoriaDTO actualizar(Integer id, SubCategoriaDTO request);

    SubCategoriaDTO obtenerPorId(Integer id);

    List<SubCategoriaDTO> listarTodas();

    List<SubCategoriaDTO> listarPorCategoria(Integer idCategoria);

    void eliminar(Integer id);
}
