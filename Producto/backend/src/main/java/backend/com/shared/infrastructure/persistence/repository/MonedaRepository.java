package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Moneda;

import java.util.List;
import java.util.Optional;

public interface MonedaRepository {

    List<Moneda> findAll();

    Optional<Moneda> findById(Integer id);

    Optional<Moneda> findByCodigoMoneda(String codigoMoneda);

    boolean existsByCodigoMoneda(String codigoMoneda);

    boolean existsById(Integer id);

    Moneda save(Moneda moneda);

    void deleteById(Integer id);
}