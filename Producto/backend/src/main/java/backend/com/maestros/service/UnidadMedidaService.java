package backend.com.maestros.service;

import backend.com.maestros.dto.request.UnidadMedidaRequestDTO;
import backend.com.maestros.dto.response.UnidadMedidaResponseDTO;

import java.util.List;

public interface UnidadMedidaService {

    UnidadMedidaResponseDTO crear(UnidadMedidaRequestDTO request);

    UnidadMedidaResponseDTO actualizar(Integer id, UnidadMedidaRequestDTO request);

    UnidadMedidaResponseDTO obtenerPorId(Integer id);

    List<UnidadMedidaResponseDTO> listarTodas();

    void eliminar(Integer id);
}
