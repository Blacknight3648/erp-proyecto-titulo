package backend.com.shared.application.service;

import backend.com.shared.domain.model.Giro;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.GiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GiroServiceImpl implements GiroService {

    private final GiroRepository giroRepository;
    private final GiroMapper giroMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Giro> listarTodos() {
        return giroRepository.findAll().stream().map(giroMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Giro> obtenerPorId(Long giroId) {
        return giroRepository.findById(giroId).map(giroMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Giro> obtenerPorCodigoActividad(String codigoActividad) {
        return giroRepository.findByCodigoActividad(codigoActividad).map(giroMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Giro> buscarPorDescripcion(String descripcionGiro) {
        return giroRepository.findByDescripcionGiroContainingIgnoreCase(descripcionGiro)
                .stream().map(giroMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Giro> obtenerPorTipoActividad(String tipoActividad) {
        return giroRepository.findByTipoActividad(tipoActividad).stream().map(giroMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Giro> obtenerPorCategoriaTributaria(String categoriaTributaria) {
        return giroRepository.findByCategoriaTributaria(categoriaTributaria)
                .stream().map(giroMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Giro> obtenerPorRegimenTributario(String regimenTributario) {
        return giroRepository.findByRegimenTributario(regimenTributario)
                .stream().map(giroMapper::toDomain).toList();
    }

    @Override
    public Giro crear(Giro giro) {
        if (giro.getCodigoActividad() != null
                && giroRepository.existsByCodigoActividad(giro.getCodigoActividad())) {
            throw new BusinessRuleException(
                    "Ya existe un giro con código de actividad: " + giro.getCodigoActividad());
        }
        GiroJpaEntity entity = giroMapper.toEntity(giro);
        entity.setGiroId(null);
        return giroMapper.toDomain(giroRepository.save(entity));
    }

    @Override
    public Giro actualizar(Long id, Giro giro) {
        GiroJpaEntity entity = giroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Giro no encontrado con ID: " + id));

        entity.setDescripcionGiro(giro.getDescripcionGiro());
        entity.setCodigoActividad(giro.getCodigoActividad());
        entity.setTipoActividad(giro.getTipoActividad());
        entity.setCategoriaTributaria(giro.getCategoriaTributaria());
        entity.setAfectoIva(giro.getAfectoIva());
        entity.setRegimenTributario(giro.getRegimenTributario());

        return giroMapper.toDomain(giroRepository.save(entity));
    }

    @Override
    public void eliminar(Long id) {
        if (!giroRepository.existsById(id)) {
            throw new EntityNotFoundException("Giro no encontrado con ID: " + id);
        }
        giroRepository.deleteById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<Giro> obtenerOCrearPorDescripcion(String descripcionGiro) {
        if (descripcionGiro == null || descripcionGiro.isBlank()) {
            return Optional.empty();
        }
        String descripcion = descripcionGiro.trim();

        Optional<GiroJpaEntity> existente = giroRepository.findByDescripcionGiroIgnoreCase(descripcion);
        if (existente.isPresent()) {
            return existente.map(giroMapper::toDomain);
        }

        GiroJpaEntity nuevo = GiroJpaEntity.builder().descripcionGiro(descripcion).build();
        try {
            return Optional.of(giroMapper.toDomain(giroRepository.save(nuevo)));
        } catch (DataIntegrityViolationException e) {
            return giroRepository.findByDescripcionGiroIgnoreCase(descripcion).map(giroMapper::toDomain);
        }
    }
}
