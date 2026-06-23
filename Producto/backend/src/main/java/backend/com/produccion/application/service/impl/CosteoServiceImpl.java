package backend.com.produccion.application.service.impl;

import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.domain.repository.SolicitudCostosRepository;
import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.dto.CosteoResumenEVNDTO;
import backend.com.produccion.application.service.CosteoService;
import backend.com.produccion.application.UseCase.CrearVersionCosteoUseCase;
import backend.com.produccion.domain.enums.AccionCosteo;
import backend.com.produccion.domain.enums.EstadoCosteo;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.CosteoItem;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.infrastructure.mapper.CosteoMapper;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CosteoServiceImpl implements CosteoService {

    private final CosteoRepository repository;
    private final CosteoMapper mapper;
    private final SolicitudCostosRepository scosRepository;
    private final ArticuloRepository articuloRepository;
    private final EvaluacionNegocioRepository evnRepository;
    private final NumeroDocumentoService numeroDocumentoService;
    private final CrearVersionCosteoUseCase crearVersionCosteoUseCase;
    private final HistorialEstadoService historialEstadoService;

    /** Tipo de entidad para el registro de historial de estado del Costeo. */
    private static final String TIPO_ENTIDAD = "COSTEO";

    private void enrichWithScosInfo(Costeo domain) {
        if (domain != null && domain.getSolicitudCostosId() != null) {
            scosRepository.findById(domain.getSolicitudCostosId()).ifPresent(scos -> {
                domain.setClienteId(scos.getClienteId());
                domain.setClienteNombre(scos.getClienteNombre());
                domain.setVendedorId(scos.getVendedorId());
                domain.setVendedorNombre(scos.getVendedorNombre());
            });
        }
    }

    /**
     * Carga el Articulo asociado a cada item a partir de su articuloId.
     * Solo aplica a TELAS y ACCESORIOS (los únicos tipos con artículo maestro en
     * BD).
     * LOGOTIPO e INSUMOS no tienen artículo aún: se omiten.
     */
    private void enrichItemsWithArticulo(Costeo domain) {
        if (domain == null || domain.getItems() == null) {
            return;
        }
        domain.getItems().forEach(item -> {
            String tipo = item.getTipoInsumo();
            boolean aplicaArticulo = "TELAS".equalsIgnoreCase(tipo) || "ACCESORIOS".equalsIgnoreCase(tipo);
            if (aplicaArticulo && item.getArticuloId() != null) {
                articuloRepository.findById(item.getArticuloId())
                        .ifPresent(item::setArticulo);
            }
        });
    }

    private CosteoDTO toEnrichedDto(Costeo domain) {
        enrichWithScosInfo(domain);
        enrichItemsWithArticulo(domain);
        return mapper.toDto(domain);
    }

    @Override
    @Transactional
    public CosteoDTO save(CosteoDTO costeoDTO) {
        Costeo domain = mapper.toDomainFromDto(costeoDTO);
        
        // Si es una actualización, conservar el estado, versión y motivo de rechazo ya persistidos en BD
        // si no fueron suministrados explícitamente en el DTO.
        if (domain.getIdCosteo() != null) {
            repository.findById(domain.getIdCosteo()).ifPresent(existing -> {
                if (costeoDTO.getEstado() == null || costeoDTO.getEstado().isBlank()) {
                    domain.setEstado(existing.getEstado());
                }
                if (costeoDTO.getVersion() == null) {
                    domain.setVersion(existing.getVersion());
                }
                if (costeoDTO.getMotivoRechazo() == null) {
                    domain.setMotivoRechazo(existing.getMotivoRechazo());
                }
            });
        }
        
        asignarNumeroSiCorresponde(domain);
        Costeo savedDomain = repository.save(domain);
        return toEnrichedDto(savedDomain);
    }

    /**
     * Garantiza que el Costeo tenga su propio número correlativo ({@code C-0000001}).
     * En creación genera uno nuevo de forma atómica dentro de esta transacción
     * (sin huecos si el save falla). En actualización conserva el número ya
     * asignado, recuperándolo de BD si el DTO no lo trajo, para no regenerarlo
     * ni provocar duplicados.
     */
    private void asignarNumeroSiCorresponde(Costeo domain) {
        if (domain == null || domain.getNumeroCosteo() != null) {
            return;
        }
        // Actualización: conservar el número ya persistido.
        if (domain.getIdCosteo() != null) {
            repository.findById(domain.getIdCosteo())
                    .map(Costeo::getNumeroCosteo)
                    .ifPresent(domain::setNumeroCosteo);
        }
        // Creación (o registro legacy sin número): asignar correlativo propio.
        if (domain.getNumeroCosteo() == null) {
            domain.setNumeroCosteo(numeroDocumentoService.siguienteFormateado("C"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<CosteoDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toEnrichedDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CosteoDTO> findById(Long id) {
        return repository.findById(id).map(this::toEnrichedDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CosteoDTO> findBySolicitudCostosId(Long scosId) {
        return repository.findBySolicitudCostosId(scosId)
                .map(this::toEnrichedDto);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<CosteoDTO> findAllBySolicitudCostosId(Long scosId) {
        return repository.findAllBySolicitudCostosId(scosId).stream()
                .map(this::toEnrichedDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<CosteoDTO> obtenerDisponiblesParaEVN() {
        java.util.Set<Long> vinculados = new java.util.HashSet<>(evnRepository.findLinkedCosteoIds());
        // Solo costeos APROBADOS pueden vincularse a un ítem de EVN/NV.
        return repository.findAll().stream()
                .filter(c -> c.getIdCosteo() != null && !vinculados.contains(c.getIdCosteo()))
                .filter(c -> c.getEstado() == backend.com.produccion.domain.enums.EstadoCosteo.APROBADO)
                .map(this::toEnrichedDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CosteoResumenEVNDTO> obtenerResumenEVN(Long idCosteo) {
        return repository.findById(idCosteo).map(costeo -> {
            enrichItemsWithArticulo(costeo);

            // Tela: primer ítem tipo TELAS; nombre y composición desde su Articulo si
            // existe
            CosteoItem itemTela = costeo.getItems() == null ? null
                    : costeo.getItems().stream()
                            .filter(i -> "TELAS".equalsIgnoreCase(i.getTipoInsumo()))
                            .findFirst()
                            .orElse(null);

            String tela = null;
            String composicion = null;
            if (itemTela != null) {
                Articulo art = itemTela.getArticulo();
                tela = art != null ? art.getNombreArticulo() : itemTela.getNombreInsumo();
                if (art != null && art.getDetalleTela() != null
                        && art.getDetalleTela().getComposicion() != null) {
                    composicion = art.getDetalleTela().getComposicion().getDescripcionComposicion();
                }
            }

            CosteoResumenEVNDTO.CosteoResumenEVNDTOBuilder builder = CosteoResumenEVNDTO.builder()
                    .idCosteo(costeo.getIdCosteo())
                    .numeroCosteo(costeo.getNumeroCosteo() != null ? costeo.getNumeroCosteo().getValue() : null)
                    .solicitudCostosId(costeo.getSolicitudCostosId())
                    .tela(tela)
                    .composicion(composicion);

            // Género y modelo (nombrePrenda) desde la SolicitudCostos
            if (costeo.getSolicitudCostosId() != null) {
                scosRepository.findById(costeo.getSolicitudCostosId()).ifPresent(scos -> {
                    builder.genero(scos.getGenero());
                    builder.modelo(scos.getNombrePrenda());
                });
            }

            return builder.build();
        });
    }

    // ----------------------------------------------------------------------
    // Transiciones del ciclo de vida. La regla de transición vive en el dominio
    // (Costeo + EstadoCosteo.puedeTransicionarA); aquí solo se orquesta carga,
    // persistencia y, para el rechazo, el versionado del snapshot.
    // ----------------------------------------------------------------------

    @Override
    @Transactional
    public CosteoDTO costear(Long id) {
        Costeo costeo = cargar(id);
        EstadoCosteo estadoAnterior = costeo.getEstado();
        costeo.marcarCosteado();
        Costeo guardado = repository.save(costeo);
        registrarHistorial(guardado, estadoAnterior, "SYSTEM", "Costeo v" + guardado.getVersion() + " costeado");
        return toEnrichedDto(guardado);
    }

    @Override
    @Transactional
    public CosteoDTO aprobar(Long id, String usuario, String rol) {
        String actor = exigirActor(usuario);
        AccionCosteo.APROBAR.validarRol(rol);
        Costeo costeo = cargar(id);
        EstadoCosteo estadoAnterior = costeo.getEstado();
        costeo.aprobar();
        Costeo guardado = repository.save(costeo);
        registrarHistorial(guardado, estadoAnterior, actor, "Costeo v" + guardado.getVersion() + " aprobado");
        return toEnrichedDto(guardado);
    }

    @Override
    @Transactional
    public CosteoDTO rechazar(Long id, String motivo, String usuario, String rol) {
        String actor = exigirActor(usuario);
        AccionCosteo.RECHAZAR.validarRol(rol);
        Costeo costeo = cargar(id);
        EstadoCosteo estadoAnterior = costeo.getEstado();
        // Snapshot técnico del costeo tal como está antes de marcarlo rechazado.
        crearVersionCosteoUseCase.ejecutar(id, "Rechazo v" + costeo.getVersion() + ": " + motivo, actor);
        costeo.rechazar(motivo);
        Costeo guardado = repository.save(costeo);
        registrarHistorial(guardado, estadoAnterior, actor, "Costeo v" + guardado.getVersion() + " rechazado: " + motivo);
        return toEnrichedDto(guardado);
    }

    @Override
    @Transactional
    public CosteoDTO reabrir(Long id) {
        Costeo costeo = cargar(id);
        EstadoCosteo estadoAnterior = costeo.getEstado();
        costeo.reabrir(); // si venía de RECHAZADO, incrementa la versión
        Costeo guardado = repository.save(costeo);
        String obs = estadoAnterior == EstadoCosteo.RECHAZADO
                ? "Retomado como v" + guardado.getVersion()
                : "Reabierto a BORRADOR (v" + guardado.getVersion() + ")";
        registrarHistorial(guardado, estadoAnterior, "SYSTEM", obs);
        return toEnrichedDto(guardado);
    }

    /** Exige la firma del actor; devuelve el usuario validado. */
    private String exigirActor(String usuario) {
        if (usuario == null || usuario.isBlank()) {
            throw new ValidationException("Se requiere identificar al usuario que firma la acción");
        }
        return usuario.trim();
    }

    private void registrarHistorial(Costeo costeo, EstadoCosteo estadoAnterior, String usuario, String observacion) {
        historialEstadoService.registrar(
                TIPO_ENTIDAD,
                costeo.getIdCosteo(),
                estadoAnterior != null ? estadoAnterior.name() : null,
                costeo.getEstado() != null ? costeo.getEstado().name() : null,
                usuario,
                observacion);
    }

    private Costeo cargar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del costeo es obligatorio");
        }
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Costeo no encontrado: " + id));
    }
}
