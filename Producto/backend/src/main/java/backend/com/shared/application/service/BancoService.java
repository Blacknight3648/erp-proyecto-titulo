package backend.com.shared.application.service;

import backend.com.shared.application.dto.BancoRequest;
import backend.com.shared.application.dto.BancoResponse;

import java.util.List;
import java.util.Optional;

public interface BancoService {
    List<BancoResponse> listarTodos();
    Optional<BancoResponse> obtenerPorId(Integer id);
    BancoResponse crear(BancoRequest request);
    BancoResponse actualizar(Integer id, BancoRequest request);
    void eliminar(Integer id);
}
