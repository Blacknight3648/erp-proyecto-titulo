package backend.com.comercial.application.service;

import backend.com.comercial.application.dto.SolicitudCotizacionesCreateDTO;
import backend.com.comercial.application.dto.SolicitudCotizacionesDTO;

import java.util.List;
import java.util.Optional;

public interface SolicitudCotizacionesService {
    SolicitudCotizacionesDTO create(SolicitudCotizacionesCreateDTO dto);

    SolicitudCotizacionesDTO update(Long id, SolicitudCotizacionesCreateDTO dto);

    Optional<SolicitudCotizacionesDTO> findById(Long id);

    List<SolicitudCotizacionesDTO> findAll();

    void delete(Long id);
}
