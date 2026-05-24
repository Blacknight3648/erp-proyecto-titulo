package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.EstadoOS;
import backend.com.produccion.domain.model.OrdenServicio;

import java.util.List;
import java.util.Optional;

public interface OrdenServicioRepository {

    OrdenServicio save(OrdenServicio ordenServicio);

    Optional<OrdenServicio> findById(Long idOS);

    List<OrdenServicio> findAll();

    List<OrdenServicio> findAllByEstado(EstadoOS estado);

    List<OrdenServicio> findAllByOpId(Long opId);

    List<OrdenServicio> findAllByProveedorId(Long proveedorId);
}
