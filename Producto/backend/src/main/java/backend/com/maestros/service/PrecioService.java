package backend.com.maestros.service;

import backend.com.maestros.dto.request.PrecioRequestDTO;
import backend.com.maestros.dto.response.PrecioResponseDTO;

import java.util.List;

public interface PrecioService {

    PrecioResponseDTO crear(PrecioRequestDTO request);

    PrecioResponseDTO actualizar(Integer idPrecio, PrecioRequestDTO request);

    PrecioResponseDTO obtenerPorId(Integer idPrecio);

    /** Todos los precios de un artículo. */
    List<PrecioResponseDTO> listarPorArticulo(Integer idArticulo);

    void eliminar(Integer idPrecio);

    /** Elimina todos los precios de un artículo (útil al desactivarlo). */
    void eliminarPorArticulo(Integer idArticulo);
}
