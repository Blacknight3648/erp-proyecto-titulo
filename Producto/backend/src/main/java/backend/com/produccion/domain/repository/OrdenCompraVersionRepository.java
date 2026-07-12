package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.OrdenCompraVersion;

import java.util.List;
import java.util.Optional;

public interface OrdenCompraVersionRepository {

    OrdenCompraVersion save(OrdenCompraVersion ocVersion);

    Optional<OrdenCompraVersion> findById(Long idOCVersion);

    Optional<OrdenCompraVersion> findUltimaPorOcId(Long ocId);

    List<OrdenCompraVersion> findAllByOcId(Long ocId);

    Integer siguienteNumeroVersion(Long ocId);
}
