package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.catalogo.FamiliaTela;
import backend.com.maestros.dto.request.FamiliaTelaRequestDTO;
import backend.com.maestros.dto.response.FamiliaTelaResponseDTO;
import backend.com.maestros.exception.DuplicadoException;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.FamiliaTelaRepository;
import backend.com.maestros.service.FamiliaTelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FamiliaTelaServiceImpl implements FamiliaTelaService {

    private final FamiliaTelaRepository familiaTelaRepository;

    @Override
    public FamiliaTelaResponseDTO crear(FamiliaTelaRequestDTO request) {
        if (familiaTelaRepository.existsByCodigoFamilia(request.getCodigoFamilia())) {
            throw new DuplicadoException("código de familia", request.getCodigoFamilia());
        }
        return toResponse(familiaTelaRepository.save(
                FamiliaTela.builder()
                        .codigoFamilia(request.getCodigoFamilia())
                        .nombreFamilia(request.getNombreFamilia())
                        .build()));
    }

    @Override
    public FamiliaTelaResponseDTO actualizar(Integer id, FamiliaTelaRequestDTO request) {
        FamiliaTela f = findOrThrow(id);
        f.setCodigoFamilia(request.getCodigoFamilia());
        f.setNombreFamilia(request.getNombreFamilia());
        return toResponse(familiaTelaRepository.save(f));
    }

    @Override @Transactional(readOnly = true)
    public FamiliaTelaResponseDTO obtenerPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Override @Transactional(readOnly = true)
    public List<FamiliaTelaResponseDTO> listarTodas() {
        return familiaTelaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public void eliminar(Integer id) {
        findOrThrow(id);
        familiaTelaRepository.deleteById(id);
    }

    private FamiliaTela findOrThrow(Integer id) {
        return familiaTelaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("FamiliaTela", id));
    }

    private FamiliaTelaResponseDTO toResponse(FamiliaTela f) {
        return FamiliaTelaResponseDTO.builder()
                .idFamiliaTela(f.getIdFamiliaTela())
                .codigoFamilia(f.getCodigoFamilia())
                .nombreFamilia(f.getNombreFamilia())
                .build();
    }
}
