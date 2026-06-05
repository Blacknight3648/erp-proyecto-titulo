package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.ComposicionDTO;
import backend.com.shared.application.service.ComposicionService;
import backend.com.shared.domain.model.Composicion;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.ComposicionMapper;
import backend.com.shared.infrastructure.persistence.repository.ComposicionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ComposicionServiceImpl implements ComposicionService {

    private final ComposicionRepository composicionRepository;
    private final ComposicionMapper mapper;

    @Override
    public ComposicionDTO crear(ComposicionDTO dto) {
        if (composicionRepository.existsByCodigoComposicion(dto.getCodigoComposicion())) {
            throw new DuplicadoException("código de composición", dto.getCodigoComposicion());
        }
        Composicion nueva = Composicion.builder()
                .codigoComposicion(dto.getCodigoComposicion())
                .descripcionComposicion(dto.getDescripcionComposicion())
                .clasificacion(dto.getClasificacion())
                .usoTipico(dto.getUsoTipico())
                .build();
        return mapper.toDTO(composicionRepository.save(nueva));
    }

    @Override
    public ComposicionDTO actualizar(Integer id, ComposicionDTO dto) {
        Composicion existente = findOrThrow(id);
        existente.setCodigoComposicion(dto.getCodigoComposicion());
        existente.setDescripcionComposicion(dto.getDescripcionComposicion());
        existente.setClasificacion(dto.getClasificacion());
        existente.setUsoTipico(dto.getUsoTipico());
        return mapper.toDTO(composicionRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public ComposicionDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComposicionDTO> listarTodas() {
        return composicionRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComposicionDTO> listarPorClasificacion(String clasificacion) {
        return composicionRepository.findByClasificacionIgnoreCase(clasificacion)
                .stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!composicionRepository.existsById(id)) {
            throw new EntityNotFoundException("Composicion con id " + id + " no encontrada");
        }
        composicionRepository.deleteById(id);
    }

    private Composicion findOrThrow(Integer id) {
        return composicionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Composicion con id " + id + " no encontrada"));
    }
}