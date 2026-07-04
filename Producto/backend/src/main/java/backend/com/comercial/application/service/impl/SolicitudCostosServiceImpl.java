package backend.com.comercial.application.service.impl;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;
import backend.com.comercial.application.dto.SCOSAccesorioDTO;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.comercial.application.dto.SCOSLogotipoDTO;
import backend.com.comercial.application.dto.SCOSTelaDTO;
import backend.com.comercial.application.dto.SolicitudCostosCreateDTO;
import backend.com.comercial.application.dto.SolicitudCostosDTO;
import backend.com.comercial.application.service.ArticuloCamposPlantillaService;
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
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.domain.enums.TipoArticulo;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
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

    private static final List<String> ALL_STANDARD_CAMPOS = List.of(
            "gorro", "cuello", "abotonaduraCierre", "cortesAplicaciones",
            "fuelles", "mangas", "puños", "pretinasRuedo", "bolsillos", "obsModelo"
    );

    private final SolicitudCostosRepository repository;
    private final CosteoService costeoService;
    private final DescripcionPlantillaService descripcionPlantillaService;
    private final DescripcionPlantillaRepository descripcionPlantillaRepository;
    private final NumeroDocumentoService numeroDocumentoService;
    private final ArticuloRepository articuloRepository;
    private final ArticuloCamposPlantillaService articuloCamposPlantillaService;

    @Override
    @Transactional
    public SolicitudCostosDTO create(SolicitudCostosCreateDTO dto) {
        // Correlativo propio y atómico (SCOS-0000001). Reemplaza el patrón
        // countByTipo()+1, que no era atómico y podía colisionar en concurrencia.
        DocumentNumber numero = numeroDocumentoService.siguienteFormateado("SCOS");

        SolicitudCostos domain = SolicitudCostos.crear(
                numero,
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
        resolverOCrearArticulo(dto);
        generatePreCosteo(saved);

        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public SolicitudCostosDTO update(Long id, SolicitudCostosCreateDTO dto) {
        SolicitudCostos existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
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
        resolverOCrearArticulo(dto);
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
        costeoService.deleteBySolicitudCostosId(id);
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
            dto.getAccesorios().forEach(a -> {
                Integer idArticulo = resolverArticuloAccesorio(a.getNombreAccesorio());
                domain.addAccesorio(new SCOSAccesorio(
                        idArticulo,
                        a.getTipo(),
                        a.getNombreAccesorio(),
                        a.getCantidad(),
                        a.getTempId()));
            });
        }

        if (dto.getLogotipos() != null) {
            dto.getLogotipos().forEach(l -> {
                // Combinamos el valor numérico y la unidad en un solo String para guardar en tamano (VARCHAR 50)
                String tamanoStr = null;
                if (l.getTamanio() != null && !l.getTamanio().isBlank()) {
                    tamanoStr = l.getTamanio().trim();
                }
                domain.addLogotipo(new SCOSLogotipo(null, l.getTipo(), l.getNombre(),
                        l.getUbicacion(), l.getColor(), tamanoStr, l.getCantidad(), l.getPrecio()));
            });
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
                        .cantidad(a.getCantidad())
                        .build()).collect(Collectors.toList()))
                .logotipos(domain.getLogotipos().stream()
                        .map(l -> new SCOSLogotipoDTO(l.getId(), l.getTipo(), l.getNombre(),
                                l.getUbicacion(), l.getColor(),
                                l.getTamano(), l.getCantidad(), l.getPrecio()))
                        .collect(Collectors.toList()))
                .descripciones(domain.getIdSCOS() != null
                        ? descripcionPlantillaService.listarPorSCOS(domain.getIdSCOS())
                        : new ArrayList<>())
                .build();
    }

    private Integer resolverArticuloAccesorio(String nombre) {
        if (nombre == null || nombre.isBlank()) return null;
        String nombreNorm = nombre.trim();
        return articuloRepository.findByTipoArticulo(TipoArticulo.ACCESORIO)
                .stream()
                .filter(a -> a.getNombreArticulo().equalsIgnoreCase(nombreNorm))
                .findFirst()
                .map(Articulo::getIdArticulo)
                .orElse(null);
    }

    private void resolverOCrearArticulo(SolicitudCostosCreateDTO dto) {
        if (!Boolean.TRUE.equals(dto.getEsPrendaNueva())) return;
        if (dto.getArticuloDescripcion() == null || dto.getArticuloDescripcion().isBlank()) return;

        String nombre = dto.getArticuloDescripcion().trim().toUpperCase();

        boolean yaExiste = articuloRepository
                .findByTipoArticulo(TipoArticulo.PRENDA_CONFECCIONAR)
                .stream()
                .anyMatch(a -> a.getNombreArticulo().equalsIgnoreCase(nombre));

        if (yaExiste) {
            throw new BusinessRuleException("La prenda \"" + nombre + "\" ya existe en el sistema");
        }

        String codigo = "ART-PRC-" + java.util.UUID.randomUUID().toString()
                .replace("-", "").substring(0, 6).toUpperCase();

        Articulo nuevo = Articulo.builder()
                .codigoArticulo(codigo)
                .nombreArticulo(nombre)
                .descripcionArticulo(dto.getNombrePrenda() != null ? dto.getNombrePrenda() : nombre)
                .tipoArticulo(TipoArticulo.PRENDA_CONFECCIONAR)
                .activo(true)
                .build();
        Articulo guardado = articuloRepository.save(nuevo);

        List<String> campos = (dto.getCamposPlantilla() != null && !dto.getCamposPlantilla().isEmpty())
                ? dto.getCamposPlantilla()
                : ALL_STANDARD_CAMPOS;

        articuloCamposPlantillaService.guardar(ArticuloCamposPlantillaDTO.builder()
                .idArticulo(guardado.getIdArticulo())
                .camposPlantilla(campos)
                .build());
    }

    private void generatePreCosteo(SolicitudCostos scos) {
        CosteoDTO costeoDTO = costeoService.findBySolicitudCostosId(scos.getIdSCOS())
                .orElse(CosteoDTO.builder()
                        .solicitudCostosId(scos.getIdSCOS())
                        .costoHilos(java.math.BigDecimal.ZERO)
                        .costoManoObra(java.math.BigDecimal.ZERO)
                        .moPrenda(java.math.BigDecimal.ZERO)
                        .moCinta(java.math.BigDecimal.ZERO)
                        .moCosturaSellada(java.math.BigDecimal.ZERO)
                        .moAcolchado(java.math.BigDecimal.ZERO)
                        .costoMoPropia(java.math.BigDecimal.ZERO)
                        .costoGratificacion(java.math.BigDecimal.ZERO)
                        .costoEtiquetas(java.math.BigDecimal.ZERO)
                        .costoEmbalaje(java.math.BigDecimal.ZERO)
                        .costoFlete(java.math.BigDecimal.ZERO)
                        .porcentajeCostoFijo(java.math.BigDecimal.ZERO)
                        .precioCinta1(java.math.BigDecimal.ZERO)
                        .cantidadCinta1(java.math.BigDecimal.ZERO)
                        .precioCinta2(java.math.BigDecimal.ZERO)
                        .cantidadCinta2(java.math.BigDecimal.ZERO)
                        .vivoReflectivo(java.math.BigDecimal.ZERO)
                        .cantidadVivo(java.math.BigDecimal.ZERO)
                        .costoTotalMateriaPrima(java.math.BigDecimal.ZERO)
                        .margenBrutoSugerido(java.math.BigDecimal.ZERO)
                        .precioVentaSugerido(java.math.BigDecimal.ZERO)
                        .items(new java.util.ArrayList<>())
                        .build());

        // El número real (COST-XXXXXXX) lo asigna CosteoServiceImpl.asignarNumeroSiCorresponde
        // al guardar, igual que cualquier otro Costeo nuevo — no se usa un placeholder aparte.
        costeoService.save(costeoDTO);
    }
}
