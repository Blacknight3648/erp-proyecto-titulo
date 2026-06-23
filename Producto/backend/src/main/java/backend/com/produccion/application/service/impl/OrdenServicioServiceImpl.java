package backend.com.produccion.application.service.impl;

import backend.com.produccion.application.UseCase.CrearOSUseCase;
import backend.com.produccion.application.dto.CrearOSRequest;
import backend.com.produccion.application.dto.DespachoOSDTO;
import backend.com.produccion.application.dto.OrdenServicioDTO;
import backend.com.produccion.application.dto.RecepcionOSDTO;
import backend.com.produccion.application.service.OrdenServicioService;
import backend.com.produccion.domain.enums.EstadoOS;
import backend.com.produccion.domain.model.DespachoOS;
import backend.com.produccion.domain.model.OrdenServicio;
import backend.com.produccion.domain.model.RecepcionOS;
import backend.com.produccion.domain.repository.OrdenServicioRepository;
import backend.com.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenServicioServiceImpl implements OrdenServicioService {

    private final OrdenServicioRepository ordenServicioRepository;
    private final CrearOSUseCase crearOSUseCase;

    @Override
    public OrdenServicioDTO crear(CrearOSRequest request) {
        return toDTO(crearOSUseCase.ejecutar(request));
    }

    @Override
    public OrdenServicioDTO registrarDespacho(Long idOS, DespachoOSDTO despachoDTO) {
        OrdenServicio os = cargar(idOS);
        DespachoOS despacho = new DespachoOS(
                null,
                idOS,
                despachoDTO.getFechaDespacho(),
                despachoDTO.getCantidadDespachada(),
                despachoDTO.getDescripcion(),
                despachoDTO.getResponsable(),
                despachoDTO.getObservaciones());
        os.registrarDespacho(despacho);
        return toDTO(ordenServicioRepository.save(os));
    }

    @Override
    public OrdenServicioDTO registrarRecepcion(Long idOS, RecepcionOSDTO recepcionDTO) {
        OrdenServicio os = cargar(idOS);
        RecepcionOS recepcion = new RecepcionOS(
                null,
                idOS,
                recepcionDTO.getFechaRecepcion(),
                recepcionDTO.getCantidadRecibida(),
                recepcionDTO.getCantidadConforme(),
                recepcionDTO.getCantidadDefectuosa(),
                recepcionDTO.getResponsable(),
                recepcionDTO.getObservaciones());
        os.registrarRecepcion(recepcion);
        return toDTO(ordenServicioRepository.save(os));
    }

    @Override
    public OrdenServicioDTO cerrar(Long idOS) {
        OrdenServicio os = cargar(idOS);
        os.cerrar();
        return toDTO(ordenServicioRepository.save(os));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenServicioDTO> obtenerPorId(Long idOS) {
        return ordenServicioRepository.findById(idOS).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenServicioDTO> listarTodas() {
        return ordenServicioRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenServicioDTO> listarPorEstado(EstadoOS estado) {
        return ordenServicioRepository.findAllByEstado(estado).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenServicioDTO> listarPorOP(Long opId) {
        return ordenServicioRepository.findAllByOpId(opId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenServicioDTO> listarPorProveedor(Long proveedorId) {
        return ordenServicioRepository.findAllByProveedorId(proveedorId).stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    private OrdenServicio cargar(Long idOS) {
        return ordenServicioRepository.findById(idOS)
                .orElseThrow(() -> new EntityNotFoundException("Orden de Servicio no encontrada: " + idOS));
    }

    private OrdenServicioDTO toDTO(OrdenServicio os) {
        if (os == null)
            return null;
        return OrdenServicioDTO.builder()
                .idOS(os.getIdOS())
                .numeroOS(os.getNumeroOS() != null ? os.getNumeroOS().getValue() : null)
                .opId(os.getOpId())
                .proveedorId(os.getProveedorId())
                .tipoServicio(os.getTipoServicio())
                .estado(os.getEstado())
                .fechaEmision(os.getFechaEmision())
                .fechaEntregaEstimada(os.getFechaEntregaEstimada())
                .descripcionTrabajo(os.getDescripcionTrabajo())
                .cantidadPactada(os.getCantidadPactada())
                .precioUnitario(os.getPrecioUnitario())
                .totalNeto(os.getTotalNeto())
                .observaciones(os.getObservaciones())
                .despachos(os.getDespachos() != null
                        ? os.getDespachos().stream().map(this::despachoToDTO).collect(Collectors.toList())
                        : List.of())
                .recepciones(os.getRecepciones() != null
                        ? os.getRecepciones().stream().map(this::recepcionToDTO).collect(Collectors.toList())
                        : List.of())
                .build();
    }

    private DespachoOSDTO despachoToDTO(DespachoOS d) {
        return DespachoOSDTO.builder()
                .idDespacho(d.getIdDespacho())
                .osId(d.getOsId())
                .fechaDespacho(d.getFechaDespacho())
                .cantidadDespachada(d.getCantidadDespachada())
                .descripcion(d.getDescripcion())
                .responsable(d.getResponsable())
                .observaciones(d.getObservaciones())
                .build();
    }

    private RecepcionOSDTO recepcionToDTO(RecepcionOS r) {
        return RecepcionOSDTO.builder()
                .idRecepcion(r.getIdRecepcion())
                .osId(r.getOsId())
                .fechaRecepcion(r.getFechaRecepcion())
                .cantidadRecibida(r.getCantidadRecibida())
                .cantidadConforme(r.getCantidadConforme())
                .cantidadDefectuosa(r.getCantidadDefectuosa())
                .responsable(r.getResponsable())
                .observaciones(r.getObservaciones())
                .build();
    }
}
