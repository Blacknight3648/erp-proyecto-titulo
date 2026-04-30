package backend.com.comercial.application.service;

import backend.com.comercial.application.dto.SolicitudCostosCreateDTO;
import backend.com.comercial.application.dto.SolicitudCostosDTO;

import java.util.List;
import java.util.Optional;

public interface SolicitudCostosService {


    SolicitudCostosDTO create(SolicitudCostosCreateDTO dto);

    SolicitudCostosDTO update(Long id, SolicitudCostosCreateDTO dto);

    Optional<SolicitudCostosDTO> findById(Long id);

    List<SolicitudCostosDTO> findAll();

    void delete(Long id);
}
