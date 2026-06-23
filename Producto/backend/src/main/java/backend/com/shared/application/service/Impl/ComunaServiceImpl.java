package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.ComunaDTO;
import backend.com.shared.application.service.ComunaService;
import backend.com.shared.domain.model.Comuna;
import backend.com.shared.domain.model.Region;
import backend.com.shared.infrastructure.persistence.repository.ComunaRepository;
import backend.com.shared.infrastructure.persistence.repository.RegionRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.ComunaNotFoundException;
import backend.com.shared.exception.RegionNotFoundException;
import backend.com.shared.infrastructure.mapper.ComunaMapper;
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
    private final ComunaMapper comunaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ComunaDTO> listarTodos() {
        return comunaRepository.findAll().stream()
                .map(comunaMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComunaDTO> obtenerPorId(Long id) {
        return comunaRepository.findById(id).map(comunaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComunaDTO> listarPorRegion(Long regionId) {
        if (!regionRepository.existsById(regionId)) {
            throw new RegionNotFoundException(regionId);
        }
        return comunaRepository.findByRegionId(regionId).stream()
                .map(comunaMapper::toDTO)
                .toList();
    }

    @Override
    public ComunaDTO crear(ComunaDTO dto) {
        Long regionId = extraerRegionId(dto);
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RegionNotFoundException(regionId));

        comunaRepository.findByNombreComuna(dto.getNombreComuna()).ifPresent(c -> {
            throw new BusinessRuleException("Ya existe una comuna con el nombre: " + dto.getNombreComuna());
        });

        Comuna comuna = Comuna.builder()
                .nombreComuna(dto.getNombreComuna().trim())
                .region(region)
                .build();

        return comunaMapper.toDTO(comunaRepository.save(comuna));
    }

    @Override
    public ComunaDTO actualizar(Long id, ComunaDTO dto) {
        Comuna existente = comunaRepository.findById(id)
                .orElseThrow(() -> new ComunaNotFoundException(id));

        Long regionId = extraerRegionId(dto);
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RegionNotFoundException(regionId));

        comunaRepository.findByNombreComuna(dto.getNombreComuna())
                .filter(c -> !c.getComunaId().equals(id))
                .ifPresent(c -> {
                    throw new BusinessRuleException("Ya existe una comuna con el nombre: " + dto.getNombreComuna());
                });

        existente.setNombreComuna(dto.getNombreComuna().trim());
        existente.setRegion(region);

        return comunaMapper.toDTO(comunaRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!comunaRepository.existsById(id)) {
            throw new ComunaNotFoundException(id);
        }
        comunaRepository.deleteById(id);
    }

    private Long extraerRegionId(ComunaDTO dto) {
        if (dto.getRegion() == null || dto.getRegion().getRegionId() == null) {
            throw new BusinessRuleException("La región de la comuna es obligatoria");
        }
        return dto.getRegion().getRegionId();
    }
}