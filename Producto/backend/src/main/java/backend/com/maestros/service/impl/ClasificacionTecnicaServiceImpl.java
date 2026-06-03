package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.catalogo.ClasificacionTecnica;
import backend.com.maestros.dto.request.ClasificacionTecnicaRequestDTO;
import backend.com.maestros.dto.response.ClasificacionTecnicaResponseDTO;
import backend.com.maestros.exception.DuplicadoException;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.ClasificacionTecnicaRepository;
import backend.com.maestros.service.ClasificacionTecnicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClasificacionTecnicaServiceImpl implements ClasificacionTecnicaService {

    private final ClasificacionTecnicaRepository clasificacionRepository;

    @Override
    public ClasificacionTecnicaResponseDTO crear(ClasificacionTecnicaRequestDTO request) {
        if (clasificacionRepository.existsByNombreClasificacion(request.getNombreClasificacion())) {
            throw new DuplicadoException("nombre de clasificación", request.getNombreClasificacion());
        }
        return toResponse(clasificacionRepository.save(
                ClasificacionTecnica.builder()
                        .nombreClasificacion(request.getNombreClasificacion())
                        .build()));
    }

    @Override
    public ClasificacionTecnicaResponseDTO actualizar(Integer id, ClasificacionTecnicaRequestDTO request) {
        ClasificacionTecnica c = findOrThrow(id);
        c.setNombreClasificacion(request.getNombreClasificacion());
        return toResponse(clasificacionRepository.save(c));
    }

    @Override @Transactional(readOnly = true)
    public ClasificacionTecnicaResponseDTO obtenerPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Override @Transactional(readOnly = true)
    public List<ClasificacionTecnicaResponseDTO> listarTodas() {
        return clasificacionRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public void eliminar(Integer id) {
        findOrThrow(id);
        clasificacionRepository.deleteById(id);
    }

    private ClasificacionTecnica findOrThrow(Integer id) {
        return clasificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("ClasificacionTecnica", id));
    }

    private ClasificacionTecnicaResponseDTO toResponse(ClasificacionTecnica c) {
        return ClasificacionTecnicaResponseDTO.builder()
                .idClasificacionTecnica(c.getIdClasificacionTecnica())
                .nombreClasificacion(c.getNombreClasificacion())
                .build();
    }
}
