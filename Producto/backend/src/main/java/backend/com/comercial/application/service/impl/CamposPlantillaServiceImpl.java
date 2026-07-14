package backend.com.comercial.application.service.impl;

import backend.com.comercial.application.dto.CamposPlantillaDTO;
import backend.com.comercial.application.service.CamposPlantillaService;
import backend.com.comercial.domain.model.CamposPlantilla;
import backend.com.comercial.domain.repository.CamposPlantillaRepository;
import backend.com.comercial.infrastructure.mapper.CamposPlantillaMapper;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CamposPlantillaServiceImpl implements CamposPlantillaService {

    private final CamposPlantillaRepository plantillaRepository;
    private final CamposPlantillaMapper mapper;

    @Override
    public CamposPlantillaDTO crear(CamposPlantillaDTO dto) {
        if (plantillaRepository.existsByNombreCampo(dto.getNombreCampo())) {
            throw new DuplicadoException("nombre de campo", dto.getNombreCampo());
        }
        CamposPlantilla nueva = CamposPlantilla.builder()
                .nombreCampo(dto.getNombreCampo())
                .build();
        return mapper.toDTO(plantillaRepository.save(nueva));
    }

    @Override
    public CamposPlantillaDTO actualizar(Long id, CamposPlantillaDTO dto) {
        CamposPlantilla existente = findOrThrow(id);
        if (!existente.getNombreCampo().equalsIgnoreCase(dto.getNombreCampo())
                && plantillaRepository.existsByNombreCampo(dto.getNombreCampo())) {
            throw new DuplicadoException("nombre de campo", dto.getNombreCampo());
        }
        existente.setNombreCampo(dto.getNombreCampo());
        return mapper.toDTO(plantillaRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public CamposPlantillaDTO obtenerPorId(Long id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CamposPlantillaDTO> listarTodas() {
        return plantillaRepository.findAllActivos().stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Long id) {
        CamposPlantilla existente = findOrThrow(id);
        existente.setActivo(false);
        plantillaRepository.save(existente);
    }

    private CamposPlantilla findOrThrow(Long id) {
        return plantillaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plantilla con id " + id + " no encontrada"));
    }
}
