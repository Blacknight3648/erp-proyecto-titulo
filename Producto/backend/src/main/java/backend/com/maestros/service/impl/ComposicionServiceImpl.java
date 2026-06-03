package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.catalogo.Composicion;
import backend.com.maestros.dto.request.ComposicionRequestDTO;
import backend.com.maestros.dto.response.ComposicionResponseDTO;
import backend.com.maestros.exception.DuplicadoException;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.ComposicionRepository;
import backend.com.maestros.service.ComposicionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ComposicionServiceImpl implements ComposicionService {

    private final ComposicionRepository composicionRepository;

    @Override
    public ComposicionResponseDTO crear(ComposicionRequestDTO request) {
        if (composicionRepository.existsByCodigoComposicion(request.getCodigoComposicion())) {
            throw new DuplicadoException("código de composición", request.getCodigoComposicion());
        }
        return toResponse(composicionRepository.save(
                Composicion.builder()
                        .codigoComposicion(request.getCodigoComposicion())
                        .descripcionComposicion(request.getDescripcionComposicion())
                        .clasificacion(request.getClasificacion())
                        .usoTipico(request.getUsoTipico())
                        .build()));
    }

    @Override
    public ComposicionResponseDTO actualizar(Integer id, ComposicionRequestDTO request) {
        Composicion c = findOrThrow(id);
        c.setCodigoComposicion(request.getCodigoComposicion());
        c.setDescripcionComposicion(request.getDescripcionComposicion());
        c.setClasificacion(request.getClasificacion());
        c.setUsoTipico(request.getUsoTipico());
        return toResponse(composicionRepository.save(c));
    }

    @Override @Transactional(readOnly = true)
    public ComposicionResponseDTO obtenerPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Override @Transactional(readOnly = true)
    public List<ComposicionResponseDTO> listarTodas() {
        return composicionRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<ComposicionResponseDTO> listarPorClasificacion(String clasificacion) {
        return composicionRepository.findByClasificacionIgnoreCase(clasificacion)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public void eliminar(Integer id) {
        findOrThrow(id);
        composicionRepository.deleteById(id);
    }

    private Composicion findOrThrow(Integer id) {
        return composicionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Composicion", id));
    }

    private ComposicionResponseDTO toResponse(Composicion c) {
        return ComposicionResponseDTO.builder()
                .idComposicion(c.getIdComposicion())
                .codigoComposicion(c.getCodigoComposicion())
                .descripcionComposicion(c.getDescripcionComposicion())
                .clasificacion(c.getClasificacion())
                .usoTipico(c.getUsoTipico())
                .build();
    }
}
