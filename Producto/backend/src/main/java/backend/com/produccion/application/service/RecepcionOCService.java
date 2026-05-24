package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.RecepcionOCDTO;

import java.util.List;
import java.util.Optional;

public interface RecepcionOCService {

    RecepcionOCDTO registrar(Long ocId, RecepcionOCDTO request);

    Optional<RecepcionOCDTO> obtenerPorId(Long idRecepcion);

    List<RecepcionOCDTO> listarPorOC(Long ocId);
}
