package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.PrecioDTO;

public interface PrecioService {

    PrecioDTO crear(PrecioDTO request);

    PrecioDTO actualizar(Integer idPrecio, PrecioDTO request);

    PrecioDTO obtenerPorId(Integer idPrecio);

    /** Todos los precios de un artículo. */
    List<PrecioDTO> listarPorArticulo(Integer idArticulo);

    void eliminar(Integer idPrecio);

    /** Elimina todos los precios de un artículo (útil al desactivarlo). */
    void eliminarPorArticulo(Integer idArticulo);
}
