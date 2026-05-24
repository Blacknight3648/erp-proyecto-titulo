package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.RecepcionOC;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RecepcionOCRepository {

    RecepcionOC save(RecepcionOC recepcionOC);

    Optional<RecepcionOC> findById(Long idRecepcion);

    List<RecepcionOC> findAllByOcId(Long ocId);

    /**
     * Suma todas las cantidadRecibida agrupadas por ocItemId,
     * para conocer cuánto se ha recepcionado acumulado por cada OCItem de una OC.
     */
    Map<Long, BigDecimal> sumarRecibidoPorOCItem(Long ocId);
}
