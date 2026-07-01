package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.domain.enums.EstadoHC;

import java.util.List;
import java.util.Optional;

public interface HojaCompraService {

    HojaCompraDTO generarDesdeOP(Long opId);

    HojaCompraDTO aprobar(Long idHC);

    HojaCompraDTO cerrar(Long idHC);

    HojaCompraDTO reabrir(Long idHC);

    Optional<HojaCompraDTO> obtenerPorId(Long idHC);

    Optional<HojaCompraDTO> obtenerPorOpId(Long opId);

    List<HojaCompraDTO> listarTodas();

    List<HojaCompraDTO> listarPorEstado(EstadoHC estado);

    HojaCompraDTO modificarItem(Long idHC, Long idHCItem, backend.com.produccion.application.dto.ActualizarHojaCompraItemRequest request);
}
