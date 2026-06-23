package backend.com.shared.application.service;

import java.util.List;

import backend.com.shared.application.dto.UnidadMedidaDTO;

public interface UnidadMedidaService {

    UnidadMedidaDTO crear(UnidadMedidaDTO request);

    UnidadMedidaDTO actualizar(Integer id, UnidadMedidaDTO request);

    UnidadMedidaDTO obtenerPorId(Integer id);

    List<UnidadMedidaDTO> listarTodas();

    void eliminar(Integer id);
}
