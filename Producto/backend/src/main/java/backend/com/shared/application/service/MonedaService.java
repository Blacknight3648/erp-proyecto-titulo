package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.MonedaDTO;

public interface MonedaService {

    MonedaDTO crear(MonedaDTO request);

    MonedaDTO actualizar(Integer id, MonedaDTO request);

    MonedaDTO obtenerPorId(Integer id);

    List<MonedaDTO> listarTodas();

    void eliminar(Integer id);
}
