package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Banco;

import java.util.List;
import java.util.Optional;

public interface BancoRepository {
    List<Banco> findAll();

    Optional<Banco> findById(Integer id);

    Optional<Banco> findByCodigoBanco(String codigoBanco);

    Banco save(Banco banco);

    void deleteById(Integer id);

    boolean existsById(Integer id);
}
