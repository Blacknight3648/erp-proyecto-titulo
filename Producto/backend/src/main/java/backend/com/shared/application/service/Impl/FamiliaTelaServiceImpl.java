package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.FamiliaTelaDTO;
import backend.com.shared.application.service.FamiliaTelaService;
import backend.com.shared.domain.model.FamiliaTela;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.FamiliaTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.FamiliaTelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FamiliaTelaServiceImpl implements FamiliaTelaService {

    private final FamiliaTelaRepository familiaTelaRepository;
    private final FamiliaTelaMapper mapper;

    @Override
    public FamiliaTelaDTO crear(FamiliaTelaDTO dto) {
        if (familiaTelaRepository.existsByCodigoFamilia(dto.getCodigoFamilia())) {
            throw new DuplicadoException("código de familia", dto.getCodigoFamilia());
        }
        FamiliaTela nueva = FamiliaTela.builder()
                .codigoFamilia(dto.getCodigoFamilia())
                .nombreFamilia(dto.getNombreFamilia())
                .build();
        return mapper.toDTO(familiaTelaRepository.save(nueva));
    }

    @Override
    public FamiliaTelaDTO actualizar(Integer id, FamiliaTelaDTO dto) {
        FamiliaTela existente = findOrThrow(id);
        existente.setCodigoFamilia(dto.getCodigoFamilia());
        existente.setNombreFamilia(dto.getNombreFamilia());
        return mapper.toDTO(familiaTelaRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public FamiliaTelaDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FamiliaTelaDTO> listarTodas() {
        return familiaTelaRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!familiaTelaRepository.existsById(id)) {
            throw new EntityNotFoundException("FamiliaTela con id " + id + " no encontrada");
        }
        familiaTelaRepository.deleteById(id);
    }

    private FamiliaTela findOrThrow(Integer id) {
        return familiaTelaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FamiliaTela con id " + id + " no encontrada"));
    }
}