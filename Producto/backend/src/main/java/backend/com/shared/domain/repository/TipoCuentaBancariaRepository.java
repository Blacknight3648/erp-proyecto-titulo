package backend.com.shared.domain.repository;

import backend.com.shared.domain.model.TipoCuentaBancaria;

import java.util.List;
import java.util.Optional;

public interface TipoCuentaBancariaRepository {
    List<TipoCuentaBancaria> findAll();
    Optional<TipoCuentaBancaria> findById(Integer id);
    TipoCuentaBancaria save(TipoCuentaBancaria tipoCuenta);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
