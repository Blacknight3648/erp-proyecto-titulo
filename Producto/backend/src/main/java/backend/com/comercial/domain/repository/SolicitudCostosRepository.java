package backend.com.comercial.domain.repository;

import backend.com.comercial.domain.model.SolicitudCostos;
import backend.com.shared.valueobjects.DocumentNumber;

import java.util.Optional;

public interface SolicitudCostosRepository {

    long countByTipo(String tipo);

    SolicitudCostos save(SolicitudCostos solicitudCostos);

    SolicitudCostos update(SolicitudCostos solicitudCostos);

    Optional<SolicitudCostos> findById(Long id);

    Optional<SolicitudCostos> findByNumero(DocumentNumber numero);

    java.util.List<SolicitudCostos> findAll();

    void deleteById(Long id);
}
