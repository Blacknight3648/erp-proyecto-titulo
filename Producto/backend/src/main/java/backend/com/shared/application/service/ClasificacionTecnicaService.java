package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.ClasificacionTecnicaDTO;

public interface ClasificacionTecnicaService {
    ClasificacionTecnicaDTO crear(ClasificacionTecnicaDTO request);

    ClasificacionTecnicaDTO actualizar(Integer id, ClasificacionTecnicaDTO request);

    ClasificacionTecnicaDTO obtenerPorId(Integer id);

    List<ClasificacionTecnicaDTO> listarTodas();

    void eliminar(Integer id);
}
