package backend.com.shared.application.service;

import backend.com.shared.infrastructure.persistence.entity.DocumentCounterJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.DocumentCounterJpaRepository;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Genera números correlativos de documentos de forma atómica.
 *
 * Uso típico desde un UseCase:
 * DocumentNumber numero = numeroDocumentoService.siguienteFormateado("NV");
 * NotaVenta nv = NotaVenta.crear(numero, ...);
 *
 * El bloqueo es por TIPO de documento. Dos llamadas concurrentes con tipos
 * diferentes ("NV" y "EVN") no se bloquean entre sí; dos llamadas con el
 * mismo tipo se serializan.
 *
 * Política de numeración: cada documento genera su PROPIO correlativo
 * independiente, sin heredar prefijos ni sufijos de documentos anteriores.
 * El formato es consecutivo puro {@code PREFIJO-0000001} (7 dígitos, sin año),
 * donde el prefijo es el propio tipo del documento.
 *
 * Tipos canónicos: "NV", "EVN", "OP", "HC", "C", "SCOS", "SCOT", "SC", "OC".
 */
@Service
@RequiredArgsConstructor
public class NumeroDocumentoService {

    /** Formato único de numeración: {@code PREFIJO-0000001}, sin año. */
    private static final String FORMATO = "%s-%07d";

    private final DocumentCounterJpaRepository repository;

    /**
     * Devuelve el siguiente número correlativo (crudo) para el tipo dado.
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

    /**
     * Reserva el siguiente correlativo y devuelve un {@link DocumentNumber} ya
     * formateado con el estándar único ({@code PREFIJO-0000001}). Es el punto
     * único de formateo: cualquier UseCase debe usar este método en vez de
     * construir el String a mano.
     */
    @Transactional
    public DocumentNumber siguienteFormateado(String tipo) {
        Long numero = siguiente(tipo);
        return new DocumentNumber(String.format(FORMATO, tipo, numero));
    }

    /**
     * Previsualiza (sin reservar ni incrementar) cuál sería el próximo número
     * para el tipo dado. Operación de solo lectura: NO consume correlativos,
     * por lo que es segura para endpoints de preview. El valor es tentativo;
     * el definitivo lo asigna {@link #siguienteFormateado(String)} al crear.
     */
    @Transactional(readOnly = true)
    public DocumentNumber previsualizar(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de documento es obligatorio");
        }
        long proximo = repository.findById(tipo)
                .map(DocumentCounterJpaEntity::getUltimoNumero)
                .orElse(0L) + 1;
        return new DocumentNumber(String.format(FORMATO, tipo, proximo));
    }
}
