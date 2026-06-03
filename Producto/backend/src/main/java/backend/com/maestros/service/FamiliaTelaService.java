package backend.com.maestros.service;

import backend.com.maestros.dto.request.FamiliaTelaRequestDTO;
import backend.com.maestros.dto.response.FamiliaTelaResponseDTO;
import java.util.List;

public interface FamiliaTelaService {
    FamiliaTelaResponseDTO crear(FamiliaTelaRequestDTO request);
    FamiliaTelaResponseDTO actualizar(Integer id, FamiliaTelaRequestDTO request);
    FamiliaTelaResponseDTO obtenerPorId(Integer id);
    List<FamiliaTelaResponseDTO> listarTodas();
    void eliminar(Integer id);
}
