package backend.com.comercial.application.service.impl;

import backend.com.comercial.application.dto.SCOSAccesorioDTO;
import backend.com.comercial.application.dto.SCOSLogotipoDTO;
import backend.com.comercial.application.dto.SCOSTelaDTO;
import backend.com.comercial.application.dto.SolicitudCostosCreateDTO;
import backend.com.comercial.application.dto.SolicitudCostosDTO;
import backend.com.comercial.application.service.DescripcionPlantillaService;
import backend.com.comercial.application.service.SolicitudCostosService;
import backend.com.comercial.domain.enums.EstadoSCOS;
import backend.com.comercial.domain.model.SCOSAccesorio;
import backend.com.comercial.domain.model.SCOSLogotipo;
import backend.com.comercial.domain.model.SCOSTela;
import backend.com.comercial.domain.model.SolicitudCostos;
import backend.com.comercial.domain.repository.DescripcionPlantillaRepository;
import backend.com.comercial.domain.repository.SolicitudCostosRepository;
import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.service.CosteoService;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudCostosServiceImpl implements SolicitudCostosService {

    private final SolicitudCostosRepository repository;
    private final CosteoService costeoService;
    private final DescripcionPlantillaService descripcionPlantillaService;
    private final DescripcionPlantillaRepository descripcionPlantillaRepository;

    @Override
    @Transactional
    public SolicitudCostosDTO create(SolicitudCostosCreateDTO dto) {
        long prefijo = repository.countByTipo("SCOS") + 1;
        String numero = "SCOS-" + String.format("%04d", prefijo);

        SolicitudCostos domain = SolicitudCostos.crear(
                new DocumentNumber(numero),
                dto.getTipo(),
                dto.getClienteId(),
                dto.getVendedorId(),
                dto.getArticuloDescripcion(),
                dto.getNombrePrenda(),
                dto.getEsMuestra() != null ? dto.getEsMuestra() : false,
                dto.getHasLogo() != null ? dto.getHasLogo() : false,
                dto.getCantidad(),
                dto.getGenero(),
                dto.getTallaje());

        mapDetailsToDomain(domain, dto);

        SolicitudCostos saved = repository.save(domain);
        persistirDescripciones(saved.getIdSCOS(), dto);
        generatePreCosteo(saved);

        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public SolicitudCostosDTO update(Long id, SolicitudCostosCreateDTO dto) {
        SolicitudCostos existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Solicitud de costos no encontrada con ID: " + id));

        SolicitudCostos updated = new SolicitudCostos(
                existing.getIdSCOS(),
                existing.getNumeroSCOS(),
                dto.getEstado() != null ? EstadoSCOS.valueOf(dto.getEstado()) : existing.getEstado(),
                dto.getTipo() != null ? dto.getTipo() : existing.getTipo(),
                dto.getClienteId() != null ? dto.getClienteId() : existing.getClienteId(),
                null,
                dto.getVendedorId() != null ? dto.getVendedorId() : existing.getVendedorId(),
                null,
                dto.getArticuloDescripcion() != null ? dto.getArticuloDescripcion() : existing.getArticuloDescripcion(),
                dto.getNombrePrenda() != null ? dto.getNombrePrenda() : existing.getNombrePrenda(),
                dto.getEsMuestra() != null ? dto.getEsMuestra() : existing.getEsMuestra(),
                dto.getHasLogo() != null ? dto.getHasLogo() : existing.getHasLogo(),
                dto.getCantidad() != null ? dto.getCantidad() : existing.getCantidad(),
                dto.getGenero() != null ? dto.getGenero() : existing.getGenero(),
                dto.getTallaje() != null ? dto.getTallaje() : existing.getTallaje(),
                existing.getFecha(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                dto.getCostoTotal() != null ? dto.getCostoTotal() : existing.getCostoTotal());

        mapDetailsToDomain(updated, dto);

        SolicitudCostos saved = repository.save(updated);
        descripcionPlantillaRepository.deleteByIdSCOS(saved.getIdSCOS());
        persistirDescripciones(saved.getIdSCOS(), dto);
        generatePreCosteo(saved);

        return mapToDTO(saved);
    }

    @Override
    public Optional<SolicitudCostosDTO> findById(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }

    @Override
    public List<SolicitudCostosDTO> findAll() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        descripcionPlantillaRepository.deleteByIdSCOS(id);
        repository.deleteById(id);
    }

    private void mapDetailsToDomain(SolicitudCostos domain, SolicitudCostosCreateDTO dto) {
        if (dto.getTelas() != null) {
            dto.getTelas().forEach(t -> domain.addTela(new SCOSTela(null,
                    t.getAplicacion(),
                    t.getNombre(),
                    null,
                    t.getProveedorReferencia(),
                    t.getComposicion(), t.getColor(), t.getPeso(),
                    t.getConsumo() != null ? t.getConsumo() : BigDecimal.ZERO,
                    t.getUnidadMedida() != null ? t.getUnidadMedida() : "mts",
                    new Money(t.getPrecioUnitario() != null ? t.getPrecioUnitario() : BigDecimal.ZERO, "CLP"),
                    t.getTempId())));
        }

        if (dto.getAccesorios() != null) {
            dto.getAccesorios().forEach(a -> domain.addAccesorio(new SCOSAccesorio(null,
                    a.getTipo(),
                    a.getNombreAccesorio(),
                    a.getCantidad(),
                    null,
                    a.getProveedorReferencia(),
                    a.getConsumo() != null ? a.getConsumo() : BigDecimal.ZERO,
                    a.getUnidadMedida() != null ? a.getUnidadMedida() : "un",
                    new Money(a.getPrecioUnitario() != null ? a.getPrecioUnitario() : BigDecimal.ZERO, "CLP"),
                    a.getTempId())));
        }

        if (dto.getLogotipos() != null) {
            dto.getLogotipos().forEach(l -> domain.addLogotipo(new SCOSLogotipo(null, l.getTipo(), l.getNombre(),
                    l.getUbicacion(), l.getColor(), l.getTamanio(), l.getCantidad(), l.getPrecio())));
        }
    }

    private void persistirDescripciones(Long idSCOS, SolicitudCostosCreateDTO dto) {
        if (dto.getDescripciones() == null) return;
        dto.getDescripciones().forEach(d -> {
            d.setIdSCOS(idSCOS);
            descripcionPlantillaService.crear(d);
        });
    }

    private SolicitudCostosDTO mapToDTO(SolicitudCostos domain) {
        return SolicitudCostosDTO.builder()
                .id(domain.getIdSCOS())
                .numero(domain.getNumeroSCOS() != null ? domain.getNumeroSCOS().getValue() : null)
                .estado(domain.getEstado() != null ? domain.getEstado().name() : null)
                .tipo(domain.getTipo())
                .clienteId(domain.getClienteId())
                .clienteNombre(domain.getClienteNombre())
                .vendedorId(domain.getVendedorId())
                .vendedorNombre(domain.getVendedorNombre())
                .articuloDescripcion(domain.getArticuloDescripcion())
                .nombrePrenda(domain.getNombrePrenda())
                .cantidad(domain.getCantidad())
                .genero(domain.getGenero())
                .tallaje(domain.getTallaje())
                .fecha(domain.getFecha())
                .esMuestra(domain.getEsMuestra())
                .hasLogo(domain.getHasLogo())
                .costoTotal(domain.getCostoTotal())
                .moneda("CLP")
                .telas(domain.getTelas().stream().map(t -> SCOSTelaDTO.builder()
                        .aplicacion(t.getAplicacion())
                        .nombre(t.getDescripcion())
                        .proveedorReferencia(t.getProveedorReferencia())
                        .composicion(t.getComposicion())
                        .color(t.getColor())
                        .peso(t.getPeso())
                        .consumo(t.getConsumo())
                        .unidadMedida(t.getUnidadMedida())
                        .precioUnitario(t.getPrecioUnitario().getAmount())
                        .precioTotal(BigDecimal.ZERO)
                        .build()).collect(Collectors.toList()))
                .accesorios(domain.getAccesorios().stream().map(a -> SCOSAccesorioDTO.builder()
                        .tipo(a.getTipo())
                        .nombreAccesorio(a.getDescripcion())
                        .proveedorReferencia(a.getProveedorReferencia())
                        .cantidad(a.getCantidad())
                        .consumo(a.getConsumo())
                        .unidadMedida(a.getUnidadMedida())
                        .precioUnitario(a.getPrecioUnitario().getAmount())
                        .precioTotal(BigDecimal.ZERO)
                        .build()).collect(Collectors.toList()))
                .logotipos(domain.getLogotipos().stream()
                        .map(l -> new SCOSLogotipoDTO(l.getTipo(), l.getNombre(),
                                l.getUbicacion(), l.getColor(),
                                l.getTamano(), l.getCantidad(), l.getPrecio()))
                        .collect(Collectors.toList()))
                .descripciones(domain.getIdSCOS() != null
                        ? descripcionPlantillaService.listarPorSCOS(domain.getIdSCOS())
                        : new ArrayList<>())
                .build();
    }

    private void generatePreCosteo(SolicitudCostos scos) {
        CosteoDTO costeoDTO = costeoService.findBySolicitudCostosId(scos.getIdSCOS())
                .orElse(CosteoDTO.builder()
                        .solicitudCostosId(scos.getIdSCOS())
                        .build());

        if (costeoDTO.getNumeroCosteo() == null) {
            costeoDTO.setNumeroCosteo("PRE-" + scos.getNumeroSCOS().getValue());
        }

        costeoService.save(costeoDTO);
    }
}
