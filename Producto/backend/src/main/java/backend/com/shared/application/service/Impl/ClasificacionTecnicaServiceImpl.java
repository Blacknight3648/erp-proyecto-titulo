package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.ClasificacionTecnicaDTO;
import backend.com.shared.application.service.ClasificacionTecnicaService;
import backend.com.shared.domain.model.ClasificacionTecnica;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.ClasificacionTecnicaMapper;
import backend.com.shared.infrastructure.persistence.repository.ClasificacionTecnicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClasificacionTecnicaServiceImpl implements ClasificacionTecnicaService {

    private final ClasificacionTecnicaRepository clasificacionRepository;
    private final ClasificacionTecnicaMapper mapper;

    @Override
    public ClasificacionTecnicaDTO crear(ClasificacionTecnicaDTO dto) {
        if (clasificacionRepository.existsByNombreClasificacion(dto.getNombreClasificacion())) {
            throw new DuplicadoException("nombre de clasificación", dto.getNombreClasificacion());
        }
        ClasificacionTecnica nueva = ClasificacionTecnica.builder()
                .nombreClasificacion(dto.getNombreClasificacion())
                .build();
        return mapper.toDTO(clasificacionRepository.save(nueva));
    }

    @Override
    public ClasificacionTecnicaDTO actualizar(Integer id, ClasificacionTecnicaDTO dto) {
        ClasificacionTecnica existente = findOrThrow(id);
        existente.setNombreClasificacion(dto.getNombreClasificacion());
        return mapper.toDTO(clasificacionRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public ClasificacionTecnicaDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClasificacionTecnicaDTO> listarTodas() {
        return clasificacionRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!clasificacionRepository.existsById(id)) {
            throw new EntityNotFoundException("ClasificacionTecnica con id " + id + " no encontrada");
        }
        clasificacionRepository.deleteById(id);
    }

    private ClasificacionTecnica findOrThrow(Integer id) {
        return clasificacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ClasificacionTecnica con id " + id + " no encontrada"));
    }
}