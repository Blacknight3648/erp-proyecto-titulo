package backend.com.comercial.domain.repository;

import backend.com.comercial.domain.model.NotaVenta;

import java.util.Optional;

public interface NotaVentaRepository {
    NotaVenta save(NotaVenta notaVenta);

    Optional<NotaVenta> findById(Long id);

    Optional<NotaVenta> findByNumero(Long numero);

    java.util.List<NotaVenta> findAll();

    /** Asigna el id de la OP a todos los ítems tipo OP de una NV. Operación atómica. */
    void vincularOpAItems(Long notaVentaId, Long opId);
}
