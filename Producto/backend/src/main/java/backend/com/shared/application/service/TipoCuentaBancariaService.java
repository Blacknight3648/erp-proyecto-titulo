package backend.com.shared.application.service;

import backend.com.shared.application.dto.TipoCuentaBancariaDTO;

import java.util.List;
import java.util.Optional;

public interface TipoCuentaBancariaService {
    List<TipoCuentaBancariaDTO> listarTodos();
    Optional<TipoCuentaBancariaDTO> obtenerPorId(Integer id);
    TipoCuentaBancariaDTO crear(TipoCuentaBancariaDTO dto);
    TipoCuentaBancariaDTO actualizar(Integer id, TipoCuentaBancariaDTO dto);
    void eliminar(Integer id);
}