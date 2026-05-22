package backend.com.shared.application.service;

import backend.com.shared.application.dto.TipoCuentaBancariaRequest;
import backend.com.shared.application.dto.TipoCuentaBancariaResponse;

import java.util.List;
import java.util.Optional;

public interface TipoCuentaBancariaService {
    List<TipoCuentaBancariaResponse> listarTodos();
    Optional<TipoCuentaBancariaResponse> obtenerPorId(Integer id);
    TipoCuentaBancariaResponse crear(TipoCuentaBancariaRequest request);
    TipoCuentaBancariaResponse actualizar(Integer id, TipoCuentaBancariaRequest request);
    void eliminar(Integer id);
}
