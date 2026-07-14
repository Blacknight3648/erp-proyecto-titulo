package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.TipoAccesorioDTO;
import backend.com.shared.application.service.CodigoGeneratorService;
import backend.com.shared.application.service.TipoAccesorioService;
import backend.com.shared.domain.model.TipoAccesorio;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.TipoAccesorioMapper;
import backend.com.shared.infrastructure.persistence.repository.TipoAccesorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoAccesorioServiceImpl implements TipoAccesorioService {

    private final TipoAccesorioRepository tipoAccesorioRepository;
    private final TipoAccesorioMapper mapper;
    private final CodigoGeneratorService codigoGeneratorService;

    @Override
    public TipoAccesorioDTO crear(TipoAccesorioDTO dto) {
        if (tipoAccesorioRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new DuplicadoException("nombre de tipo de accesorio", dto.getNombre());
        }
        String codigo = codigoGeneratorService.generarPorAbreviatura(
                "", dto.getNombre(), tipoAccesorioRepository::existsByCodigo);
        TipoAccesorio nuevo = TipoAccesorio.builder()
                .codigo(codigo)
                .nombre(dto.getNombre())
                .build();
        return mapper.toDTO(tipoAccesorioRepository.save(nuevo));
    }

    @Override
    public TipoAccesorioDTO actualizar(Integer id, TipoAccesorioDTO dto) {
        TipoAccesorio existente = findOrThrow(id);
        existente.setNombre(dto.getNombre());
        return mapper.toDTO(tipoAccesorioRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public TipoAccesorioDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoAccesorioDTO> listarTodos() {
        return tipoAccesorioRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!tipoAccesorioRepository.existsById(id)) {
            throw new EntityNotFoundException("TipoAccesorio con id " + id + " no encontrado");
        }
        tipoAccesorioRepository.deleteById(id);
    }

    private TipoAccesorio findOrThrow(Integer id) {
        return tipoAccesorioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TipoAccesorio con id " + id + " no encontrado"));
    }
}
