package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.ModeloDTO;
import backend.com.shared.application.service.ModeloService;
import backend.com.shared.application.service.CodigoGeneratorService;
import backend.com.shared.domain.model.Modelo;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.ModeloMapper;
import backend.com.shared.infrastructure.persistence.repository.ModeloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ModeloServiceImpl implements ModeloService {

    private static final String PREFIJO_CODIGO = "MOD-";

    private final ModeloRepository modeloRepository;
    private final ModeloMapper mapper;
    private final CodigoGeneratorService codigoGeneratorService;

    @Override
    public ModeloDTO crear(ModeloDTO dto) {
        if (modeloRepository.existsByNombreModelo(dto.getNombreModelo())) {
            throw new DuplicadoException("nombre de modelo", dto.getNombreModelo());
        }
        String codigo = codigoGeneratorService.generarPorAbreviatura(
                PREFIJO_CODIGO, dto.getNombreModelo(), modeloRepository::existsByCodigoModelo);
        Modelo nuevo = Modelo.builder()
                .codigoModelo(codigo)
                .nombreModelo(dto.getNombreModelo())
                .build();
        return mapper.toDTO(modeloRepository.save(nuevo));
    }

    @Override
    public ModeloDTO actualizar(Integer id, ModeloDTO dto) {
        Modelo existente = findOrThrow(id);

        if (!existente.getCodigoModelo().equals(dto.getCodigoModelo())
                && modeloRepository.existsByCodigoModelo(dto.getCodigoModelo())) {
            throw new DuplicadoException("código de modelo", dto.getCodigoModelo());
        }
        if (!existente.getNombreModelo().equals(dto.getNombreModelo())
                && modeloRepository.existsByNombreModelo(dto.getNombreModelo())) {
            throw new DuplicadoException("nombre de modelo", dto.getNombreModelo());
        }

        existente.setCodigoModelo(dto.getCodigoModelo());
        existente.setNombreModelo(dto.getNombreModelo());
        return mapper.toDTO(modeloRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public ModeloDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModeloDTO> listarTodos() {
        return modeloRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!modeloRepository.existsById(id)) {
            throw new EntityNotFoundException("Modelo con id " + id + " no encontrado");
        }
        modeloRepository.deleteById(id);
    }

    private Modelo findOrThrow(Integer id) {
        return modeloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modelo con id " + id + " no encontrado"));
    }
}
