package backend.com.shared.application.service.Impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import backend.com.shared.application.dto.TipoContactoDTO;
import backend.com.shared.application.service.TipoContactoService;
import backend.com.shared.domain.model.TipoContacto;
import backend.com.shared.infrastructure.mapper.TipoContactoMapper;
import backend.com.shared.infrastructure.persistence.entity.TipoContactoJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.TipoContactoRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TipoContactoServiceImpl implements TipoContactoService {

    private final TipoContactoRepository tipoContactoRepository;
    private final TipoContactoMapper tipoContactoMapper;

    @Override
    public List<TipoContactoDTO> listarTodos() {
        return tipoContactoRepository.findAll().stream()
                .map(entity -> tipoContactoMapper.toDTO(tipoContactoMapper.toDomain(entity)))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TipoContactoDTO> obtenerPorId(Long id) {
        return tipoContactoRepository.findById(id)
                .map(entity -> tipoContactoMapper.toDTO(tipoContactoMapper.toDomain(entity)));
    }

    @Override
    public Optional<TipoContactoDTO> obtenerPorNombreTipoContacto(String nombreTipoContacto) {
        return tipoContactoRepository.findByDescripcionTipoContactoIgnoreCase(nombreTipoContacto)
                .map(entity -> tipoContactoMapper.toDTO(tipoContactoMapper.toDomain(entity)));
    }

    @Override
    public TipoContactoDTO crearTipoContacto(TipoContactoDTO tipoContactoDTO) {
        TipoContacto domain = tipoContactoMapper.toDomain(tipoContactoDTO);
        TipoContactoJpaEntity entity = tipoContactoMapper.toEntity(domain);
        TipoContactoJpaEntity saved = tipoContactoRepository.save(entity);
        return tipoContactoMapper.toDTO(tipoContactoMapper.toDomain(saved));
    }

    @Override
    public TipoContactoDTO actualizarTipoContacto(Long id, TipoContactoDTO tipoContactoDTO) {
        TipoContactoJpaEntity entity = tipoContactoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tipo de contacto no encontrado con ID: " + id));

        TipoContacto domainToUpdate = tipoContactoMapper.toDomain(tipoContactoDTO);
        entity.setDescripcionTipoContacto(domainToUpdate.getDescripcionTipoContacto());

        TipoContactoJpaEntity saved = tipoContactoRepository.save(entity);
        return tipoContactoMapper.toDTO(tipoContactoMapper.toDomain(saved));
    }

    @Override
    public void eliminarTipoContacto(Long id) {
        if (!tipoContactoRepository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de contacto no encontrado con ID: " + id);
        }
        tipoContactoRepository.deleteById(id);
    }

    @Override
    public Optional<TipoContactoDTO> obtenerOCrearPorNombreTipoContacto(String nombreTipoContacto) {
        if (nombreTipoContacto == null || nombreTipoContacto.isBlank()) {
            return Optional.empty();
        }
        String nombre = nombreTipoContacto.trim();

        Optional<TipoContactoJpaEntity> existente =
                tipoContactoRepository.findByDescripcionTipoContactoIgnoreCase(nombre);
        if (existente.isPresent()) {
            return existente.map(entity -> tipoContactoMapper.toDTO(tipoContactoMapper.toDomain(entity)));
        }

        TipoContactoJpaEntity nuevo = TipoContactoJpaEntity.builder()
                .descripcionTipoContacto(nombre)
                .build();
        try {
            TipoContactoJpaEntity saved = tipoContactoRepository.save(nuevo);
            return Optional.of(tipoContactoMapper.toDTO(tipoContactoMapper.toDomain(saved)));
        } catch (DataIntegrityViolationException e) {
            return tipoContactoRepository.findByDescripcionTipoContactoIgnoreCase(nombre)
                    .map(entity -> tipoContactoMapper.toDTO(tipoContactoMapper.toDomain(entity)));
        }
    }
}
