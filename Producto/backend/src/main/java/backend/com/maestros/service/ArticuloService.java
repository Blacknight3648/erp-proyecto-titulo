package backend.com.maestros.service;

import backend.com.maestros.domain.entity.Articulo;
import backend.com.maestros.domain.enums.TipoArticulo;
import backend.com.maestros.dto.articuloDto.ArticuloCreateRequestDTO;
import backend.com.maestros.dto.articuloDto.ArticuloUpdateRequestDTO;

import java.util.List;

public interface ArticuloService {

    Articulo crearArticulo(ArticuloCreateRequestDTO request);

    Articulo actualizarArticulo(Integer idArticulo,
                                ArticuloUpdateRequestDTO request);

    Articulo obtenerPorId(Integer idArticulo);

    Articulo obtenerPorCodigo(String codigoArticulo);

    List<Articulo> listarTodos();

    List<Articulo> listarActivos();

    List<Articulo> listarPorTipo(TipoArticulo tipoArticulo);

    List<Articulo> buscarPorNombre(String nombre);

    void eliminarArticulo(Integer idArticulo);

}
