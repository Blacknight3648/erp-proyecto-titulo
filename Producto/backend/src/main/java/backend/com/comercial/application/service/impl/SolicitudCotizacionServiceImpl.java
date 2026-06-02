package backend.com.comercial.application.service.impl;

import backend.com.comercial.domain.model.*;
import backend.com.comercial.domain.repository.SolicitudCotizacionRepository;
import backend.com.comercial.application.dto.*;
import backend.com.comercial.application.service.SolicitudCotizacionesService;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudCotizacionServiceImpl implements SolicitudCotizacionesService {

        private final SolicitudCotizacionRepository repository;

        @Override
        @Transactional
        public SolicitudCotizacionesDTO create(SolicitudCotizacionesCreateDTO dto) {
                long siguiente = repository.countByTipo("SCOT") + 1;
                String randomNum = "SCOT-" + String.format("%04d", siguiente);

                SolicitudCotizacion domain = SolicitudCotizacion.crear(
                                new DocumentNumber(randomNum),
                                dto.getTipo(),
                                dto.getClienteId(),
                                dto.getVendedorId(),
                                null,
                                dto.getArticuloDescripcion(),
                                dto.getEsMuestra() != null ? dto.getEsMuestra() : false,
                                dto.getHasLogo() != null ? dto.getHasLogo() : false,
                                dto.getCantidad());

                mapDetailsToDomain(domain, dto);

                SolicitudCotizacion saved = repository.save(domain);
                return mapToDTO(saved);
        }

        @Override
        @Transactional
        public SolicitudCotizacionesDTO update(Long id, SolicitudCotizacionesCreateDTO dto) {
                SolicitudCotizacion existing = repository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Cotización no encontrada con ID: " + id));

                SolicitudCotizacion updated = new SolicitudCotizacion(
                                existing.getIdSCOT(),
                                existing.getNumeroSCOT(),
                                dto.getEstado() != null ? dto.getEstado() : existing.getEstado(),
                                dto.getTipo() != null ? dto.getTipo() : existing.getTipo(),
                                dto.getClienteId() != null ? dto.getClienteId() : existing.getClienteId(),
                                dto.getVendedorId() != null ? dto.getVendedorId() : existing.getVendedorId(),
                                existing.getEspecificacionTecnicaId(),
                                dto.getArticuloDescripcion() != null ? dto.getArticuloDescripcion()
                                                : existing.getArticuloDescripcion(),
                                dto.getEsMuestra() != null ? dto.getEsMuestra() : existing.getEsMuestra(),
                                dto.getHasLogo() != null ? dto.getHasLogo() : existing.getHasLogo(),
                                dto.getCantidad() != null ? dto.getCantidad() : existing.getCantidad(),
                                existing.getFecha(),
                                new ArrayList<>(), new ArrayList<>());

                mapDetailsToDomain(updated, dto);

                SolicitudCotizacion result = repository.save(updated);
                return mapToDTO(result);
        }

        @Override
        public Optional<SolicitudCotizacionesDTO> findById(Long id) {
                return repository.findById(id).map(this::mapToDTO);
        }

        @Override
        public List<SolicitudCotizacionesDTO> findAll() {
                return repository.findAll().stream()
                                .map(this::mapToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public void delete(Long id) {
                repository.deleteById(id);
        }

        private void mapDetailsToDomain(SolicitudCotizacion domain, SolicitudCotizacionesCreateDTO dto) {
                if (dto.getPrendas() != null) {
                        dto.getPrendas().forEach(p -> domain.addPrenda(
                                        new SCOTPrendaLista(null, p.getNombre(), p.getCantidad(), p.getTalla(),
                                                        p.getColor(),
                                                        p.getProveedorReferencia(), p.getLinkReferencia(),
                                                        p.getComposicion(), p.getPeso(),
                                                        p.getObservaciones(),
                                                        new Money(p.getPrecioUnitario() != null
                                                                        ? new java.math.BigDecimal(
                                                                                        p.getPrecioUnitario())
                                                                        : java.math.BigDecimal.ZERO, "CLP"))));
                }

                if (dto.getLogotipos() != null) {
                        dto.getLogotipos().forEach(
                                        l -> domain.addLogotipo(new SCOSLogotipo(null, l.getTipo(), l.getNombre(),
                                                        l.getUbicacion(), l.getColor(), l.getTamanio(), l.getCantidad(),
                                                        l.getPrecio())));
                }
        }

        private SolicitudCotizacionesDTO mapToDTO(SolicitudCotizacion domain) {
                return SolicitudCotizacionesDTO.builder()
                                .id(domain.getIdSCOT())
                                .numero(domain.getNumeroSCOT() != null ? domain.getNumeroSCOT().getValue() : null)
                                .estado(domain.getEstado())
                                .tipo(domain.getTipo())
                                .clienteId(domain.getClienteId())
                                .vendedorId(domain.getVendedorId())
                                .articuloDescripcion(domain.getArticuloDescripcion())
                                .cantidad(domain.getCantidad())
                                .fecha(domain.getFecha())
                                .esMuestra(domain.getEsMuestra())
                                .hasLogo(domain.getHasLogo())
                                .costoTotal(java.math.BigDecimal.ZERO)
                                .moneda("CLP")
                                .prendas(domain.getPrendas().stream().map(p -> {
                                        double precioUnitario = p.getPrecioUnitario() != null
                                                        ? p.getPrecioUnitario().getAmount().doubleValue()
                                                        : 0.0;
                                        double cantidad = p.getCantidad() != null ? p.getCantidad().doubleValue() : 0.0;
                                        return new SCOTPrendaListaDTO(
                                                        p.getNombre(), p.getCantidad(), p.getTalla(), p.getColor(),
                                                        p.getProveedorReferencia(),
                                                        p.getLinkReferencia(), p.getComposicion(), p.getPeso(),
                                                        p.getObservaciones(),
                                                        precioUnitario, precioUnitario * cantidad);
                                }).collect(Collectors.toList()))
                                .logotipos(domain.getLogotipos().stream()
                                                .map(l -> new SCOSLogotipoDTO(l.getTipo(), l.getNombre(),
                                                                l.getUbicacion(), l.getColor(),
                                                                l.getTamano(), l.getCantidad(), l.getPrecio()))
                                                .collect(Collectors.toList()))
                                .build();
        }
}
