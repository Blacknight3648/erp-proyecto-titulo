package backend.com.maestros.service;

import backend.com.maestros.dto.request.MonedaRequestDTO;
import backend.com.maestros.dto.response.MonedaResponseDTO;

import java.util.List;

public interface MonedaService {

    MonedaResponseDTO crear(MonedaRequestDTO request);

    MonedaResponseDTO actualizar(Integer id, MonedaRequestDTO request);

    MonedaResponseDTO obtenerPorId(Integer id);

    List<MonedaResponseDTO> listarTodas();

    void eliminar(Integer id);
}
