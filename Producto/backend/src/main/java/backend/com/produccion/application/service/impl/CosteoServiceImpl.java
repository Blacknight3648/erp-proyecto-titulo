package backend.com.produccion.application.service.impl;

import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.domain.repository.SolicitudCostosRepository;
import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.dto.CosteoResumenEVNDTO;
import backend.com.produccion.application.service.CosteoService;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.CosteoItem;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.infrastructure.mapper.CosteoMapper;
import backend.com.shared.domain.model.Articulo;
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
     * Solo aplica a TELAS y ACCESORIOS (los únicos tipos con artículo maestro en BD).
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
        Costeo savedDomain = repository.save(domain);
        return toEnrichedDto(savedDomain);
    }

    @Override
    public java.util.List<CosteoDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toEnrichedDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<CosteoDTO> findBySolicitudCostosId(Long scosId) {
        return repository.findBySolicitudCostosId(scosId)
                .map(this::toEnrichedDto);
    }

    @Override
    public java.util.List<CosteoDTO> findAllBySolicitudCostosId(Long scosId) {
        return repository.findAllBySolicitudCostosId(scosId).stream()
                .map(this::toEnrichedDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<CosteoDTO> obtenerDisponiblesParaEVN() {
        java.util.Set<Long> vinculados = new java.util.HashSet<>(evnRepository.findLinkedCosteoIds());
        return repository.findAll().stream()
                .filter(c -> c.getIdCosteo() != null && !vinculados.contains(c.getIdCosteo()))
                .map(this::toEnrichedDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CosteoResumenEVNDTO> obtenerResumenEVN(Long idCosteo) {
        return repository.findById(idCosteo).map(costeo -> {
            enrichItemsWithArticulo(costeo);

            // Tela: primer ítem tipo TELAS; nombre y composición desde su Articulo si existe
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
}
