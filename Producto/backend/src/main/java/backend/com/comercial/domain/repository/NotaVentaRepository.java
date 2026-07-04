package backend.com.comercial.domain.repository;

import backend.com.comercial.domain.model.NotaVenta;

import java.util.Optional;

public interface NotaVentaRepository {
    NotaVenta save(NotaVenta notaVenta);

    Optional<NotaVenta> findById(Long id);

    Optional<NotaVenta> findByNumero(Long numero);

    java.util.List<NotaVenta> findAll();

    /** Asigna el id de la OP a un ítem puntual de la NV (cada ítem OP tiene su propia OP). */
    void vincularOpAItem(Long itemId, Long opId);

    void delete(NotaVenta notaVenta);
}
