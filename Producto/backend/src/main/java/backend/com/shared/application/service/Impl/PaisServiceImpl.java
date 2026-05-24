package backend.com.shared.application.service.Impl;

import backend.com.shared.application.dto.PaisDTO;
import backend.com.shared.application.service.PaisService;
import backend.com.shared.domain.model.Pais;
import backend.com.shared.infrastructure.persistence.repository.PaisRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.PaisNotFoundException;
import backend.com.shared.infrastructure.mapper.PaisMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaisServiceImpl implements PaisService {

    private final PaisRepository paisRepository;
    private final PaisMapper paisMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PaisDTO> listarTodos() {
        return paisRepository.findAll().stream()
                .map(paisMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaisDTO> obtenerPorId(Integer id) {
        return paisRepository.findById(id).map(paisMapper::toDTO);
    }

    @Override
    public PaisDTO crear(PaisDTO dto) {
        paisRepository.findByNombrePais(dto.getNombrePais()).ifPresent(p -> {
            throw new BusinessRuleException("Ya existe un país con el nombre: " + dto.getNombrePais());
        });

        Pais pais = Pais.builder()
                .nombrePais(dto.getNombrePais().trim())
                .build();

        return paisMapper.toDTO(paisRepository.save(pais));
    }

    @Override
    public PaisDTO actualizar(Integer id, PaisDTO dto) {
        Pais existente = paisRepository.findById(id)
                .orElseThrow(() -> new PaisNotFoundException(id));

        paisRepository.findByNombrePais(dto.getNombrePais())
                .filter(p -> !p.getIdPais().equals(id.longValue()))
                .ifPresent(p -> {
                    throw new BusinessRuleException("Ya existe un país con el nombre: " + dto.getNombrePais());
                });

        existente.setNombrePais(dto.getNombrePais().trim());

        return paisMapper.toDTO(paisRepository.save(existente));
    }

    @Override
    public void eliminar(Integer id) {
        if (!paisRepository.existsById(id)) {
            throw new PaisNotFoundException(id);
        }
        paisRepository.deleteById(id);
    }
}