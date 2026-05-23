package backend.com.shared.application.service.Impl;

import backend.com.shared.application.dto.RegionDTO;
import backend.com.shared.application.service.RegionService;
import backend.com.shared.domain.model.Pais;
import backend.com.shared.domain.model.Region;
import backend.com.shared.domain.repository.PaisRepository;
import backend.com.shared.domain.repository.RegionRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.PaisNotFoundException;
import backend.com.shared.exception.RegionNotFoundException;
import backend.com.shared.infrastructure.mapper.RegionMapper;
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
    private final RegionMapper regionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RegionDTO> listarTodos() {
        return regionRepository.findAll().stream()
                .map(regionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegionDTO> obtenerPorId(Long id) {
        return regionRepository.findById(id).map(regionMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegionDTO> listarPorPais(Integer paisId) {
        if (!paisRepository.existsById(paisId)) {
            throw new PaisNotFoundException(paisId);
        }
        return regionRepository.findByPaisId(paisId).stream()
                .map(regionMapper::toDTO)
                .toList();
    }

    @Override
    public RegionDTO crear(RegionDTO dto) {
        Integer paisId = extraerPaisId(dto);
        Pais pais = paisRepository.findById(paisId)
                .orElseThrow(() -> new PaisNotFoundException(paisId));

        regionRepository.findByNombreRegion(dto.getNombreRegion()).ifPresent(r -> {
            throw new BusinessRuleException("Ya existe una región con el nombre: " + dto.getNombreRegion());
        });

        Region region = Region.builder()
                .nombreRegion(dto.getNombreRegion().trim())
                .pais(pais)
                .build();

        return regionMapper.toDTO(regionRepository.save(region));
    }

    @Override
    public RegionDTO actualizar(Long id, RegionDTO dto) {
        Region existente = regionRepository.findById(id)
                .orElseThrow(() -> new RegionNotFoundException(id));

        Integer paisId = extraerPaisId(dto);
        Pais pais = paisRepository.findById(paisId)
                .orElseThrow(() -> new PaisNotFoundException(paisId));

        regionRepository.findByNombreRegion(dto.getNombreRegion())
                .filter(r -> !r.getRegionId().equals(id))
                .ifPresent(r -> {
                    throw new BusinessRuleException("Ya existe una región con el nombre: " + dto.getNombreRegion());
                });

        existente.setNombreRegion(dto.getNombreRegion().trim());
        existente.setPais(pais);

        return regionMapper.toDTO(regionRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new RegionNotFoundException(id);
        }
        regionRepository.deleteById(id);
    }

    private Integer extraerPaisId(RegionDTO dto) {
        if (dto.getPais() == null || dto.getPais().getIdPais() == null) {
            throw new BusinessRuleException("El país de la región es obligatorio");
        }
        return dto.getPais().getIdPais().intValue();
    }
}