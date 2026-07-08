package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.RubroDTO;
import backend.com.shared.application.service.RubroService;
import backend.com.shared.domain.model.Rubro;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.infrastructure.persistence.repository.RubroRepository;
import backend.com.shared.infrastructure.mapper.RubroMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RubroServiceImpl implements RubroService {

    private final RubroRepository rubroRepository;
    private final RubroMapper rubroMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RubroDTO> getAllRubros() {
        List<Rubro> rubros = rubroRepository.findAll();

        List<RubroDTO> rubroDTOs = rubros.stream()
                .map(rubroMapper::toDTO)
                .collect(Collectors.toList());

        return rubroDTOs;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RubroDTO> getRubroById(Long id) {
        return rubroRepository.findById(id)
                .map(rubroMapper::toDTO);
    }

    @Override
    @Transactional
    public RubroDTO createRubro(RubroDTO rubroDTO) {
        Rubro rubro = rubroMapper.toDomain(rubroDTO);
        String siglaRubro = normalizarSigla(rubro.getSiglaRubro());
        validarSiglaRubro(siglaRubro, null);
        rubro.setSiglaRubro(siglaRubro);
        Rubro savedRubro = rubroRepository.save(rubro);
        return rubroMapper.toDTO(savedRubro);
    }

    @Override
    @Transactional
    public Optional<RubroDTO> updateRubro(Long id, RubroDTO rubroDTO) {
        return rubroRepository.findById(id)
                .map(existingRubro -> {
                    String siglaRubro = normalizarSigla(rubroDTO.getSiglaRubro());
                    validarSiglaRubro(siglaRubro, id);
                    existingRubro.setNombreRubro(rubroDTO.getNombreRubro());
                    existingRubro.setDescripcionRubro(rubroDTO.getDescripcionRubro());
                    existingRubro.setSiglaRubro(siglaRubro);
                    Rubro updatedRubro = rubroRepository.save(existingRubro);
                    return rubroMapper.toDTO(updatedRubro);
                });
    }

    private String normalizarSigla(String siglaRubro) {
        return siglaRubro == null ? null : siglaRubro.trim().toUpperCase();
    }

    private void validarSiglaRubro(String siglaRubro, Long idActual) {
        if (siglaRubro == null || siglaRubro.isBlank()) {
            return;
        }
        if (siglaRubro.length() < 2 || siglaRubro.length() > 10) {
            throw new BusinessRuleException("La sigla del rubro debe tener entre 2 y 10 caracteres");
        }
        rubroRepository.findBySiglaRubro(siglaRubro)
                .filter(rubroExistente -> !rubroExistente.getRubroId().equals(idActual))
                .ifPresent(rubroExistente -> {
                    throw new DuplicadoException("sigla de rubro", siglaRubro);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RubroDTO> getRubroByNombreRubro(String nombreRubro) {
        return rubroRepository.findByNombreRubro(nombreRubro)
                .map(rubroMapper::toDTO);
    }

    @Override
    @Transactional
    public void deleteRubro(Long id) {
        rubroRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RubroDTO> searchRubros(String query, int limit) {
        List<Rubro> rubros = rubroRepository.findAllByOrderByNombreRubroAsc();
        return rubros.stream()
                .map(rubroMapper::toDTO)
                .collect(Collectors.toList());
    }
}
