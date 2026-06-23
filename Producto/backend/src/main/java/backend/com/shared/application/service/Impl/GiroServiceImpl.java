package backend.com.shared.application.service.impl;

import backend.com.shared.application.service.GiroService;
import backend.com.shared.domain.model.Giro;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.GiroRepository;
import backend.com.shared.infrastructure.persistence.entity.RubroJpaEntity;
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
    public Optional<Giro> obtenerPorCodigoSii(String codigoSii) {
        return giroRepository.findByCodigoSii(codigoSii).map(giroMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Giro> buscarPorNombreGiro(String nombreGiro) {
        return giroRepository.findByNombreGiroContainingIgnoreCase(nombreGiro)
                .stream().map(giroMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Giro> obtenerPorDescripcionGiro(String descripcionGiro) {
        return giroRepository.findByDescripcionGiroContainingIgnoreCase(descripcionGiro)
                .stream().map(giroMapper::toDomain).toList();
    }

    @Override
    public Giro crear(Giro giro) {
        if (giro.getCodigoSii() != null
                && giroRepository.existsByCodigoSii(giro.getCodigoSii())) {
            throw new BusinessRuleException(
                    "Ya existe un giro con código de actividad: " + giro.getCodigoSii());
        }
        GiroJpaEntity entity = giroMapper.toEntity(giro);
        entity.setGiroId(null);
        return giroMapper.toDomain(giroRepository.save(entity));
    }

    @Override
    public Giro actualizar(Long id, Giro giro) {
        GiroJpaEntity entity = giroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Giro no encontrado con ID: " + id));

        entity.setCodigoSii(giro.getCodigoSii());
        entity.setNombreGiro(giro.getNombreGiro());
        entity.setDescripcionGiro(giro.getDescripcionGiro());
        entity.setRubro(RubroJpaEntity.builder().rubroId(giro.getRubro().getRubroId()).build());

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
    public Optional<Giro> obtenerOCrearPorNombreGiro(String nombreGiro) {
        if (nombreGiro == null || nombreGiro.isBlank()) {
            return Optional.empty();
        }
        String nombre = nombreGiro.trim();

        Optional<GiroJpaEntity> existente = giroRepository.findByNombreGiroIgnoreCase(nombre);
        if (existente.isPresent()) {
            return existente.map(giroMapper::toDomain);
        }

        GiroJpaEntity nuevo = GiroJpaEntity.builder().nombreGiro(nombre).build();
        try {
            return Optional.of(giroMapper.toDomain(giroRepository.save(nuevo)));
        } catch (DataIntegrityViolationException e) {
            return giroRepository.findByNombreGiroIgnoreCase(nombre).map(giroMapper::toDomain);
        }
    }
}
