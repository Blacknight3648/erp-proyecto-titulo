package backend.com.shared.application.service;

import backend.com.shared.domain.model.Sigla;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.SiglaMapper;
import backend.com.shared.infrastructure.persistence.entity.SiglaJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.SiglaRepository;
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
public class SiglaServiceImpl implements SiglaService {

    private final SiglaRepository siglaRepository;
    private final SiglaMapper siglaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Sigla> obtenerTodos() {
        return siglaRepository.findAll().stream().map(siglaMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sigla> obtenerPorId(Long siglaId) {
        return siglaRepository.findById(siglaId).map(siglaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sigla> obtenerPorDescripcionSigla(String descripcionSigla) {
        return siglaRepository.findByDescripcionSiglaContainingIgnoreCase(descripcionSigla)
                .stream().map(siglaMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sigla> obtenerPorSiglaAbreviatura(String siglaAbreviatura) {
        return siglaRepository.findBySiglaAbreviatura(siglaAbreviatura).map(siglaMapper::toDomain);
    }

    @Override
    public Sigla crear(Sigla sigla) {
        if (sigla.getDescripcionSigla() != null
                && siglaRepository.existsByDescripcionSigla(sigla.getDescripcionSigla())) {
            throw new BusinessRuleException(
                    "Ya existe una sigla con la descripción: " + sigla.getDescripcionSigla());
        }
        if (sigla.getSiglaAbreviatura() != null
                && siglaRepository.existsBySiglaAbreviatura(sigla.getSiglaAbreviatura())) {
            throw new BusinessRuleException(
                    "Ya existe una sigla con la abreviatura: " + sigla.getSiglaAbreviatura());
        }
        SiglaJpaEntity entity = siglaMapper.toEntity(sigla);
        entity.setSiglaId(null);
        return siglaMapper.toDomain(siglaRepository.save(entity));
    }

    @Override
    public Sigla actualizar(Long siglaId, Sigla sigla) {
        SiglaJpaEntity entity = siglaRepository.findById(siglaId)
                .orElseThrow(() -> new EntityNotFoundException("Sigla no encontrada con ID: " + siglaId));

        if (sigla.getDescripcionSigla() != null
                && !sigla.getDescripcionSigla().equalsIgnoreCase(entity.getDescripcionSigla())) {
            if (siglaRepository.existsByDescripcionSigla(sigla.getDescripcionSigla())) {
                throw new BusinessRuleException(
                        "Ya existe una sigla con la descripción: " + sigla.getDescripcionSigla());
            }
            entity.setDescripcionSigla(sigla.getDescripcionSigla());
        }

        if (sigla.getSiglaAbreviatura() != null
                && !sigla.getSiglaAbreviatura().equalsIgnoreCase(entity.getSiglaAbreviatura())) {
            if (siglaRepository.existsBySiglaAbreviatura(sigla.getSiglaAbreviatura())) {
                throw new BusinessRuleException(
                        "Ya existe una sigla con la abreviatura: " + sigla.getSiglaAbreviatura());
            }
            entity.setSiglaAbreviatura(sigla.getSiglaAbreviatura());
        }

        return siglaMapper.toDomain(siglaRepository.save(entity));
    }

    @Override
    public void eliminar(Long siglaId) {
        if (!siglaRepository.existsById(siglaId)) {
            throw new EntityNotFoundException("Sigla no encontrada con ID: " + siglaId);
        }
        siglaRepository.deleteById(siglaId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<Sigla> obtenerOCrearPorAbreviatura(String siglaAbreviatura) {
        if (siglaAbreviatura == null || siglaAbreviatura.isBlank()) {
            return Optional.empty();
        }
        String abreviatura = siglaAbreviatura.trim();

        Optional<SiglaJpaEntity> existente = siglaRepository.findBySiglaAbreviaturaIgnoreCase(abreviatura);
        if (existente.isPresent()) {
            return existente.map(siglaMapper::toDomain);
        }

        SiglaJpaEntity nueva = SiglaJpaEntity.builder().siglaAbreviatura(abreviatura).build();
        try {
            return Optional.of(siglaMapper.toDomain(siglaRepository.save(nueva)));
        } catch (DataIntegrityViolationException e) {
            return siglaRepository.findBySiglaAbreviaturaIgnoreCase(abreviatura).map(siglaMapper::toDomain);
        }
    }
}
