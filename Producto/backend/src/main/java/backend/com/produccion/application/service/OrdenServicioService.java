package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.CrearOSRequest;
import backend.com.produccion.application.dto.DespachoOSDTO;
import backend.com.produccion.application.dto.OrdenServicioDTO;
import backend.com.produccion.application.dto.RecepcionOSDTO;
import backend.com.produccion.domain.model.EstadoOS;

import java.util.List;
import java.util.Optional;

public interface OrdenServicioService {

    OrdenServicioDTO crear(CrearOSRequest request);

    OrdenServicioDTO registrarDespacho(Long idOS, DespachoOSDTO despachoDTO);

    OrdenServicioDTO registrarRecepcion(Long idOS, RecepcionOSDTO recepcionDTO);

    OrdenServicioDTO cerrar(Long idOS);

    Optional<OrdenServicioDTO> obtenerPorId(Long idOS);

    List<OrdenServicioDTO> listarTodas();

    List<OrdenServicioDTO> listarPorEstado(EstadoOS estado);

    List<OrdenServicioDTO> listarPorOP(Long opId);

    List<OrdenServicioDTO> listarPorProveedor(Long proveedorId);
}
