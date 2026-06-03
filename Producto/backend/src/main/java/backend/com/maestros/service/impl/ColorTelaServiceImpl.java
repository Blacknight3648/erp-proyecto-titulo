package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.catalogo.ColorTela;
import backend.com.maestros.dto.request.ColorTelaRequestDTO;
import backend.com.maestros.dto.response.ColorTelaResponseDTO;
import backend.com.maestros.exception.DuplicadoException;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.ColorTelaRepository;
import backend.com.maestros.service.ColorTelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ColorTelaServiceImpl implements ColorTelaService {

    private final ColorTelaRepository colorTelaRepository;

    @Override
    public ColorTelaResponseDTO crear(ColorTelaRequestDTO request) {
        if (colorTelaRepository.existsByCodigoColor(request.getCodigoColor())) {
            throw new DuplicadoException("código de color", request.getCodigoColor());
        }
        return toResponse(colorTelaRepository.save(
                ColorTela.builder()
                        .codigoColor(request.getCodigoColor())
                        .descripcionColor(request.getDescripcionColor())
                        .esPantone(request.getEsPantone() != null ? request.getEsPantone() : false)
                        .build()));
    }

    @Override
    public ColorTelaResponseDTO actualizar(Integer id, ColorTelaRequestDTO request) {
        ColorTela c = findOrThrow(id);
        c.setCodigoColor(request.getCodigoColor());
        c.setDescripcionColor(request.getDescripcionColor());
        c.setEsPantone(request.getEsPantone() != null ? request.getEsPantone() : false);
        return toResponse(colorTelaRepository.save(c));
    }

    @Override @Transactional(readOnly = true)
    public ColorTelaResponseDTO obtenerPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Override @Transactional(readOnly = true)
    public List<ColorTelaResponseDTO> listarTodos() {
        return colorTelaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<ColorTelaResponseDTO> buscarPorDescripcion(String descripcion) {
        return colorTelaRepository.findByDescripcionColorContainingIgnoreCase(descripcion)
                .stream().map(this::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<ColorTelaResponseDTO> listarPantone() {
        return colorTelaRepository.findByEsPantone(true)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public void eliminar(Integer id) {
        findOrThrow(id);
        colorTelaRepository.deleteById(id);
    }

    private ColorTela findOrThrow(Integer id) {
        return colorTelaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("ColorTela", id));
    }

    private ColorTelaResponseDTO toResponse(ColorTela c) {
        return ColorTelaResponseDTO.builder()
                .idColor(c.getIdColor())
                .codigoColor(c.getCodigoColor())
                .descripcionColor(c.getDescripcionColor())
                .esPantone(c.getEsPantone())
                .build();
    }
}
