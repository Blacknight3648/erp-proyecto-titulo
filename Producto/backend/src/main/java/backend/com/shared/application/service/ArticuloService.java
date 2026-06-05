package backend.com.shared.application.service;

import backend.com.shared.application.dto.ArticuloDTO;
import backend.com.shared.domain.enums.TipoArticulo;

import java.util.List;

public interface ArticuloService {

    ArticuloDTO crearArticulo(ArticuloDTO request);

    ArticuloDTO actualizarArticulo(Integer idArticulo, ArticuloDTO request);

    ArticuloDTO obtenerPorId(Integer idArticulo);

    ArticuloDTO obtenerPorCodigo(String codigoArticulo);

    List<ArticuloDTO> listarTodos();

    List<ArticuloDTO> listarActivos();

    List<ArticuloDTO> listarPorTipo(TipoArticulo tipoArticulo);

    List<ArticuloDTO> buscarPorNombre(String nombre);

    void eliminarArticulo(Integer idArticulo);
}