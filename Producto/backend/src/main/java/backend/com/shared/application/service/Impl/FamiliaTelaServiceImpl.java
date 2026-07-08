package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.FamiliaTelaDTO;
import backend.com.shared.application.service.CodigoGeneratorService;
import backend.com.shared.application.service.FamiliaTelaService;
import backend.com.shared.domain.model.FamiliaTela;
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

    private static final String PREFIJO_CODIGO = "T-";

    private final FamiliaTelaRepository familiaTelaRepository;
    private final FamiliaTelaMapper mapper;
    private final CodigoGeneratorService codigoGeneratorService;

    @Override
    public FamiliaTelaDTO crear(FamiliaTelaDTO dto) {
        String codigo = codigoGeneratorService.generarPorAbreviatura(
                PREFIJO_CODIGO, dto.getNombreFamilia(), familiaTelaRepository::existsByCodigoFamilia);
        FamiliaTela nueva = FamiliaTela.builder()
                .codigoFamilia(codigo)
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