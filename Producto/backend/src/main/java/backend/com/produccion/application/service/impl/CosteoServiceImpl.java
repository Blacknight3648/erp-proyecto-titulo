package backend.com.produccion.application.service.impl;

import backend.com.comercial.domain.repository.SolicitudCostosRepository;
import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.service.CosteoService;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.infrastructure.mapper.CosteoMapper;
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
     * Carga el Articulo asociado a cada item a partir de su insumoId.
     * Solo lectura: si el insumo no corresponde a un artículo existente, se omite.
     */
    private void enrichItemsWithArticulo(Costeo domain) {
        if (domain == null || domain.getItems() == null) {
            return;
        }
        domain.getItems().forEach(item -> {
            if (item.getInsumoId() != null) {
                articuloRepository.findById(item.getInsumoId().intValue())
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
}
