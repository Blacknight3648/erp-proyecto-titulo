package backend.com.shared.application.service;

import backend.com.shared.application.dto.BancoDTO;

import java.util.List;
import java.util.Optional;

public interface BancoService {
    List<BancoDTO> listarTodos();
    Optional<BancoDTO> obtenerPorId(Integer id);
    BancoDTO crear(BancoDTO dto);
    BancoDTO actualizar(Integer id, BancoDTO dto);
    void eliminar(Integer id);
}
