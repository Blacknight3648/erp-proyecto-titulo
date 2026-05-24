package backend.com.shared.application.service;

import backend.com.shared.infrastructure.persistence.entity.DocumentCounterJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.DocumentCounterJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Genera números correlativos de documentos de forma atómica.
 *
 * Uso típico desde un UseCase:
 * Long numero = numeroDocumentoService.siguiente("NV");
 * NotaVenta nv = NotaVenta.crear(new DocumentNumber(numero), ...);
 *
 * El bloqueo es por TIPO de documento. Dos llamadas concurrentes con tipos
 * diferentes ("NV" y "EVN") no se bloquean entre sí; dos llamadas con el
 * mismo tipo se serializan.
 *
 * Tipos canónicos: "NV", "EVN", "OP", "SCOS", "SCOT".
 */
@Service
@RequiredArgsConstructor
public class NumeroDocumentoService {

    private final DocumentCounterJpaRepository repository;

    /**
     * Devuelve el siguiente número correlativo para el tipo dado.
     * Se ejecuta dentro de la transacción del llamador (propagación REQUIRED por
     * defecto)
     * para garantizar que, si la creación del documento falla y se revierte, el
     * incremento del contador también se revierte — sin huecos en la numeración.
     */
    @Transactional
    public Long siguiente(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de documento es obligatorio");
        }
        DocumentCounterJpaEntity counter = repository.findForUpdate(tipo)
                .orElseGet(() -> repository.save(new DocumentCounterJpaEntity(tipo, 0L)));
        counter.setUltimoNumero(counter.getUltimoNumero() + 1);
        return repository.save(counter).getUltimoNumero();
    }
}
