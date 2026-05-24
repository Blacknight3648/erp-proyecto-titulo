package backend.com.shared.application.service.Impl;

import backend.com.shared.application.dto.TipoDireccionDTO;
import backend.com.shared.application.service.TipoDireccionService;
import backend.com.shared.domain.model.TipoDireccion;
import backend.com.shared.infrastructure.persistence.repository.TipoDireccionRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.TipoDireccionNotFoundException;
import backend.com.shared.infrastructure.mapper.TipoDireccionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoDireccionServiceImpl implements TipoDireccionService {

    private final TipoDireccionRepository tipoDireccionRepository;
    private final TipoDireccionMapper tipoDireccionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TipoDireccionDTO> listarTodos() {
        return tipoDireccionRepository.findAll().stream()
                .map(tipoDireccionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoDireccionDTO> obtenerPorId(Integer id) {
        return tipoDireccionRepository.findById(id).map(tipoDireccionMapper::toDTO);
    }

    @Override
    public TipoDireccionDTO crear(TipoDireccionDTO dto) {
        boolean descripcionDuplicada = tipoDireccionRepository.findAll().stream()
                .anyMatch(t -> t.getDescripcion().equalsIgnoreCase(dto.getDescripcion().trim()));

        if (descripcionDuplicada) {
            throw new BusinessRuleException(
                    "Ya existe un tipo de dirección con la descripción: " + dto.getDescripcion());
        }

        TipoDireccion tipoDireccion = TipoDireccion.builder()
                .descripcion(dto.getDescripcion().trim())
                .build();

        return tipoDireccionMapper.toDTO(tipoDireccionRepository.save(tipoDireccion));
    }

    @Override
    public TipoDireccionDTO actualizar(Integer id, TipoDireccionDTO dto) {
        TipoDireccion existente = tipoDireccionRepository.findById(id)
                .orElseThrow(() -> new TipoDireccionNotFoundException(id));

        boolean descripcionDuplicada = tipoDireccionRepository.findAll().stream()
                .filter(t -> !t.getTipoDireccionId().equals(id))
                .anyMatch(t -> t.getDescripcion().equalsIgnoreCase(dto.getDescripcion().trim()));

        if (descripcionDuplicada) {
            throw new BusinessRuleException(
                    "Ya existe un tipo de dirección con la descripción: " + dto.getDescripcion());
        }

        existente.setDescripcion(dto.getDescripcion().trim());

        return tipoDireccionMapper.toDTO(tipoDireccionRepository.save(existente));
    }

    @Override
    public void eliminar(Integer id) {
        if (!tipoDireccionRepository.existsById(id)) {
            throw new TipoDireccionNotFoundException(id);
        }
        tipoDireccionRepository.deleteById(id);
    }
}