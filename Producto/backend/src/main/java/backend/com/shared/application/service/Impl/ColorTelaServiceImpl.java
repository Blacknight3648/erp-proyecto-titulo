package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.ColorTelaDTO;
import backend.com.shared.application.service.ColorTelaService;
import backend.com.shared.domain.model.ColorTela;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.ColorTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.ColorTelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ColorTelaServiceImpl implements ColorTelaService {

    private final ColorTelaRepository colorTelaRepository;
    private final ColorTelaMapper mapper;

    @Override
    public ColorTelaDTO crear(ColorTelaDTO dto) {
        if (colorTelaRepository.existsByCodigoColor(dto.getCodigoColor())) {
            throw new DuplicadoException("código de color", dto.getCodigoColor());
        }
        ColorTela nuevo = ColorTela.builder()
                .codigoColor(dto.getCodigoColor())
                .descripcionColor(dto.getDescripcionColor())
                .esPantone(dto.getEsPantone() != null ? dto.getEsPantone() : false)
                .build();
        return mapper.toDTO(colorTelaRepository.save(nuevo));
    }

    @Override
    public ColorTelaDTO actualizar(Integer id, ColorTelaDTO dto) {
        ColorTela existente = findOrThrow(id);
        existente.setCodigoColor(dto.getCodigoColor());
        existente.setDescripcionColor(dto.getDescripcionColor());
        existente.setEsPantone(dto.getEsPantone() != null ? dto.getEsPantone() : false);
        return mapper.toDTO(colorTelaRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public ColorTelaDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColorTelaDTO> listarTodos() {
        return colorTelaRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColorTelaDTO> buscarPorDescripcion(String descripcion) {
        return colorTelaRepository.findByDescripcionColorContainingIgnoreCase(descripcion)
                .stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColorTelaDTO> listarPantone() {
        return colorTelaRepository.findByEsPantone(true)
                .stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!colorTelaRepository.existsById(id)) {
            throw new EntityNotFoundException("ColorTela con id " + id + " no encontrado");
        }
        colorTelaRepository.deleteById(id);
    }

    private ColorTela findOrThrow(Integer id) {
        return colorTelaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ColorTela con id " + id + " no encontrado"));
    }
}