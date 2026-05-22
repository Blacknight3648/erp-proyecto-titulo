package backend.com.shared.application.service.Impl;

import backend.com.shared.application.dto.RegionRequest;
import backend.com.shared.application.dto.RegionResponse;
import backend.com.shared.application.service.RegionService;
import backend.com.shared.domain.model.Pais;
import backend.com.shared.domain.model.Region;
import backend.com.shared.domain.repository.PaisRepository;
import backend.com.shared.domain.repository.RegionRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.PaisNotFoundException;
import backend.com.shared.exception.RegionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final PaisRepository paisRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RegionResponse> listarTodos() {
        return regionRepository.findAll().stream()
                .map(RegionResponse::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegionResponse> obtenerPorId(Long id) {
        return regionRepository.findById(id).map(RegionResponse::fromDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegionResponse> listarPorPais(Integer paisId) {
        if (!paisRepository.existsById(paisId)) {
            throw new PaisNotFoundException(paisId);
        }
        return regionRepository.findByPaisId(paisId).stream()
                .map(RegionResponse::fromDomain)
                .toList();
    }

    @Override
    public RegionResponse crear(RegionRequest request) {
        Pais pais = paisRepository.findById(request.getPaisId())
                .orElseThrow(() -> new PaisNotFoundException(request.getPaisId()));

        regionRepository.findByNombreRegion(request.getNombreRegion()).ifPresent(r -> {
            throw new BusinessRuleException("Ya existe una región con el nombre: " + request.getNombreRegion());
        });

        Region region = Region.builder()
                .nombreRegion(request.getNombreRegion().trim())
                .pais(pais)
                .build();

        return RegionResponse.fromDomain(regionRepository.save(region));
    }

    @Override
    public RegionResponse actualizar(Long id, RegionRequest request) {
        Region existente = regionRepository.findById(id)
                .orElseThrow(() -> new RegionNotFoundException(id));

        Pais pais = paisRepository.findById(request.getPaisId())
                .orElseThrow(() -> new PaisNotFoundException(request.getPaisId()));

        regionRepository.findByNombreRegion(request.getNombreRegion())
                .filter(r -> !r.getRegionId().equals(id))
                .ifPresent(r -> {
                    throw new BusinessRuleException("Ya existe una región con el nombre: " + request.getNombreRegion());
                });

        existente.setNombreRegion(request.getNombreRegion().trim());
        existente.setPais(pais);

        return RegionResponse.fromDomain(regionRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new RegionNotFoundException(id);
        }
        regionRepository.deleteById(id);
    }
}
