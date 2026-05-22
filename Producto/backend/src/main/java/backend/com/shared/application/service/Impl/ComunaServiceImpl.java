package backend.com.shared.application.service.Impl;

import backend.com.shared.application.dto.ComunaRequest;
import backend.com.shared.application.dto.ComunaResponse;
import backend.com.shared.application.service.ComunaService;
import backend.com.shared.domain.model.Comuna;
import backend.com.shared.domain.model.Region;
import backend.com.shared.domain.repository.ComunaRepository;
import backend.com.shared.domain.repository.RegionRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.ComunaNotFoundException;
import backend.com.shared.exception.RegionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ComunaServiceImpl implements ComunaService {

    private final ComunaRepository comunaRepository;
    private final RegionRepository regionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ComunaResponse> listarTodos() {
        return comunaRepository.findAll().stream()
                .map(ComunaResponse::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComunaResponse> obtenerPorId(Long id) {
        return comunaRepository.findById(id).map(ComunaResponse::fromDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComunaResponse> listarPorRegion(Long regionId) {
        if (!regionRepository.existsById(regionId)) {
            throw new RegionNotFoundException(regionId);
        }
        return comunaRepository.findByRegionId(regionId).stream()
                .map(ComunaResponse::fromDomain)
                .toList();
    }

    @Override
    public ComunaResponse crear(ComunaRequest request) {
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new RegionNotFoundException(request.getRegionId()));

        comunaRepository.findByNombreComuna(request.getNombreComuna()).ifPresent(c -> {
            throw new BusinessRuleException("Ya existe una comuna con el nombre: " + request.getNombreComuna());
        });

        Comuna comuna = Comuna.builder()
                .nombreComuna(request.getNombreComuna().trim())
                .region(region)
                .build();

        return ComunaResponse.fromDomain(comunaRepository.save(comuna));
    }

    @Override
    public ComunaResponse actualizar(Long id, ComunaRequest request) {
        Comuna existente = comunaRepository.findById(id)
                .orElseThrow(() -> new ComunaNotFoundException(id));

        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new RegionNotFoundException(request.getRegionId()));

        comunaRepository.findByNombreComuna(request.getNombreComuna())
                .filter(c -> !c.getComunaId().equals(id))
                .ifPresent(c -> {
                    throw new BusinessRuleException("Ya existe una comuna con el nombre: " + request.getNombreComuna());
                });

        existente.setNombreComuna(request.getNombreComuna().trim());
        existente.setRegion(region);

        return ComunaResponse.fromDomain(comunaRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!comunaRepository.existsById(id)) {
            throw new ComunaNotFoundException(id);
        }
        comunaRepository.deleteById(id);
    }
}
