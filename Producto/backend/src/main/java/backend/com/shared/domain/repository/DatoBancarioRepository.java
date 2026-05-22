package backend.com.shared.domain.repository;

import backend.com.shared.domain.model.DatoBancario;

import java.util.List;
import java.util.Optional;

public interface DatoBancarioRepository {
    List<DatoBancario> findAll();
    Optional<DatoBancario> findById(Integer id);
    List<DatoBancario> findByBancoId(Integer bancoId);
    DatoBancario save(DatoBancario datoBancario);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
