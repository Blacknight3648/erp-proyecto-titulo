package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.FamiliaTelaDTO;

public interface FamiliaTelaService {
    FamiliaTelaDTO crear(FamiliaTelaDTO request);

    FamiliaTelaDTO actualizar(Integer id, FamiliaTelaDTO request);

    FamiliaTelaDTO obtenerPorId(Integer id);

    List<FamiliaTelaDTO> listarTodas();

    void eliminar(Integer id);
}
