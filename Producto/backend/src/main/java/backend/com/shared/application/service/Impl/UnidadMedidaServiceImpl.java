package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.UnidadMedidaDTO;
import backend.com.shared.application.service.UnidadMedidaService;
import backend.com.shared.domain.model.UnidadMedida;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.UnidadMedidaMapper;
import backend.com.shared.infrastructure.persistence.repository.UnidadMedidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UnidadMedidaServiceImpl implements UnidadMedidaService {

    private final UnidadMedidaRepository unidadMedidaRepository;
    private final UnidadMedidaMapper mapper;

    @Override
    public UnidadMedidaDTO crear(UnidadMedidaDTO dto) {
        if (unidadMedidaRepository.existsByAbreviatura(dto.getAbreviatura())) {
            throw new DuplicadoException("abreviatura", dto.getAbreviatura());
        }
        UnidadMedida nueva = UnidadMedida.builder()
                .nombreUnidad(dto.getNombreUnidad())
                .abreviatura(dto.getAbreviatura())
                .build();
        return mapper.toDTO(unidadMedidaRepository.save(nueva));
    }

    @Override
    public UnidadMedidaDTO actualizar(Integer id, UnidadMedidaDTO dto) {
        UnidadMedida existente = findOrThrow(id);
        existente.setNombreUnidad(dto.getNombreUnidad());
        existente.setAbreviatura(dto.getAbreviatura());
        return mapper.toDTO(unidadMedidaRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public UnidadMedidaDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadMedidaDTO> listarTodas() {
        return unidadMedidaRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!unidadMedidaRepository.existsById(id)) {
            throw new EntityNotFoundException("UnidadMedida con id " + id + " no encontrada");
        }
        unidadMedidaRepository.deleteById(id);
    }

    private UnidadMedida findOrThrow(Integer id) {
        return unidadMedidaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UnidadMedida con id " + id + " no encontrada"));
    }
}