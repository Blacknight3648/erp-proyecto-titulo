package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.AtributoTecnicoDTO;

public interface AtributoTecnicoService {
    AtributoTecnicoDTO crear(AtributoTecnicoDTO request);

    AtributoTecnicoDTO actualizar(Integer id, AtributoTecnicoDTO request);

    AtributoTecnicoDTO obtenerPorId(Integer id);

    List<AtributoTecnicoDTO> listarTodos();

    List<AtributoTecnicoDTO> listarPorClasificacion(String clasificacion);

    void eliminar(Integer id);
}
