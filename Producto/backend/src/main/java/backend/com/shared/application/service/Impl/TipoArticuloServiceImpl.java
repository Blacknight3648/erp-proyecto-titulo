package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.TipoArticuloDTO;
import backend.com.shared.application.service.TipoArticuloService;
import backend.com.shared.domain.model.TipoArticulo;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.TipoArticuloMapper;
import backend.com.shared.infrastructure.persistence.repository.TipoArticuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoArticuloServiceImpl implements TipoArticuloService {

    private final TipoArticuloRepository tipoArticuloRepository;
    private final TipoArticuloMapper mapper;

    @Override
    public TipoArticuloDTO crear(TipoArticuloDTO dto) {
        if (tipoArticuloRepository.existsById(dto.getIdTipoArticulo())) {
            throw new DuplicadoException("id de tipo artículo", String.valueOf(dto.getIdTipoArticulo()));
        }
        if (tipoArticuloRepository.existsByCodigo(dto.getCodigo())) {
            throw new DuplicadoException("código de tipo artículo", dto.getCodigo());
        }
        TipoArticulo nuevo = TipoArticulo.builder()
                .idTipoArticulo(dto.getIdTipoArticulo())
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .build();
        return mapper.toDTO(tipoArticuloRepository.save(nuevo));
    }

    @Override
    public TipoArticuloDTO actualizar(Integer id, TipoArticuloDTO dto) {
        TipoArticulo existente = findOrThrow(id);
        existente.setNombre(dto.getNombre());
        return mapper.toDTO(tipoArticuloRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public TipoArticuloDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoArticuloDTO> listarTodos() {
        return tipoArticuloRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!tipoArticuloRepository.existsById(id)) {
            throw new EntityNotFoundException("TipoArticulo con id " + id + " no encontrado");
        }
        tipoArticuloRepository.deleteById(id);
    }

    private TipoArticulo findOrThrow(Integer id) {
        return tipoArticuloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TipoArticulo con id " + id + " no encontrado"));
    }
}
