package backend.com.comercial.domain.repository;

import backend.com.comercial.domain.model.NotaVenta;

import java.util.Optional;

public interface NotaVentaRepository {
    NotaVenta save(NotaVenta notaVenta);

    Optional<NotaVenta> findById(Long id);

    Optional<NotaVenta> findByNumero(Long numero);

    java.util.List<NotaVenta> findAll();
}
