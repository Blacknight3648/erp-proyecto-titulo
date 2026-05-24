package backend.com.shared.application.service.Impl;

import backend.com.shared.application.dto.DireccionDTO;
import backend.com.shared.application.service.DireccionService;
import backend.com.shared.domain.model.Comuna;
import backend.com.shared.domain.model.Direccion;
import backend.com.shared.domain.model.TipoDireccion;
import backend.com.shared.infrastructure.persistence.repository.ComunaRepository;
import backend.com.shared.infrastructure.persistence.repository.DireccionRepository;
import backend.com.shared.infrastructure.persistence.repository.TipoDireccionRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.ComunaNotFoundException;
import backend.com.shared.exception.DireccionNotFoundException;
import backend.com.shared.exception.TipoDireccionNotFoundException;
import backend.com.shared.infrastructure.mapper.DireccionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository direccionRepository;
    private final ComunaRepository comunaRepository;
    private final TipoDireccionRepository tipoDireccionRepository;
    private final DireccionMapper direccionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DireccionDTO> listarTodos() {
        return direccionRepository.findAll().stream()
                .map(direccionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DireccionDTO> obtenerPorId(Long id) {
        return direccionRepository.findById(id).map(direccionMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DireccionDTO> listarPorComuna(Long comunaId) {
        if (!comunaRepository.existsById(comunaId)) {
            throw new ComunaNotFoundException(comunaId);
        }
        return direccionRepository.findByComunaId(comunaId).stream()
                .map(direccionMapper::toDTO)
                .toList();
    }

    @Override
    public DireccionDTO crear(DireccionDTO dto) {
        Integer tipoDireccionId = extraerTipoDireccionId(dto);
        Long comunaId = extraerComunaId(dto);

        TipoDireccion tipoDireccion = tipoDireccionRepository.findById(tipoDireccionId)
                .orElseThrow(() -> new TipoDireccionNotFoundException(tipoDireccionId));

        Comuna comuna = comunaRepository.findById(comunaId)
                .orElseThrow(() -> new ComunaNotFoundException(comunaId));

        Direccion direccion = Direccion.builder()
                .calle(dto.getCalle().trim())
                .numero(dto.getNumero().trim())
                .depto(dto.getDepto() != null ? dto.getDepto().trim() : null)
                .tipoDireccion(tipoDireccion)
                .comuna(comuna)
                .build();

        return direccionMapper.toDTO(direccionRepository.save(direccion));
    }

    @Override
    public DireccionDTO actualizar(Long id, DireccionDTO dto) {
        Direccion existente = direccionRepository.findById(id)
                .orElseThrow(() -> new DireccionNotFoundException(id));

        Integer tipoDireccionId = extraerTipoDireccionId(dto);
        Long comunaId = extraerComunaId(dto);

        TipoDireccion tipoDireccion = tipoDireccionRepository.findById(tipoDireccionId)
                .orElseThrow(() -> new TipoDireccionNotFoundException(tipoDireccionId));

        Comuna comuna = comunaRepository.findById(comunaId)
                .orElseThrow(() -> new ComunaNotFoundException(comunaId));

        existente.setCalle(dto.getCalle().trim());
        existente.setNumero(dto.getNumero().trim());
        existente.setDepto(dto.getDepto() != null ? dto.getDepto().trim() : null);
        existente.setTipoDireccion(tipoDireccion);
        existente.setComuna(comuna);

        return direccionMapper.toDTO(direccionRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new DireccionNotFoundException(id);
        }
        direccionRepository.deleteById(id);
    }

    private Integer extraerTipoDireccionId(DireccionDTO dto) {
        if (dto.getTipoDireccion() == null || dto.getTipoDireccion().getTipoDireccionId() == null) {
            throw new BusinessRuleException("El tipo de dirección es obligatorio");
        }
        return dto.getTipoDireccion().getTipoDireccionId();
    }

    private Long extraerComunaId(DireccionDTO dto) {
        if (dto.getComuna() == null || dto.getComuna().getComunaId() == null) {
            throw new BusinessRuleException("La comuna de la dirección es obligatoria");
        }
        return dto.getComuna().getComunaId();
    }
}