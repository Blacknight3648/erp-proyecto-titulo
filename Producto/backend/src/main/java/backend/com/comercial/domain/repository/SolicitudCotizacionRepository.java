package backend.com.comercial.domain.repository;

import backend.com.comercial.domain.model.SolicitudCotizacion;

import java.util.Optional;

public interface SolicitudCotizacionRepository {

    long countByTipo(String tipo);  
    SolicitudCotizacion save(SolicitudCotizacion solicitudCotizacion);
    SolicitudCotizacion update(SolicitudCotizacion solicitudCotizacion);
    Optional<SolicitudCotizacion> findById(Long id);
    java.util.List<SolicitudCotizacion> findAll();
    void deleteById(Long id);
}


