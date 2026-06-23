package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.SubCategoriaTelaDTO;
import backend.com.shared.application.service.SubCategoriaTelaService;
import backend.com.shared.domain.model.CategoriaTela;
import backend.com.shared.domain.model.SubCategoriaTela;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.SubCategoriaTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.CategoriaTelaRepository;
import backend.com.shared.infrastructure.persistence.repository.SubCategoriaTelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubCategoriaTelaServiceImpl implements SubCategoriaTelaService {

    private final SubCategoriaTelaRepository subCategoriaTelaRepository;
    private final CategoriaTelaRepository categoriaTelaRepository;
    private final SubCategoriaTelaMapper mapper;

    @Override
    public SubCategoriaTelaDTO crear(SubCategoriaTelaDTO dto) {
        if (subCategoriaTelaRepository.existsByCodigoSubCategoriaTela(dto.getCodigoSubCategoriaTela())) {
            throw new DuplicadoException("código de subcategoría tela", dto.getCodigoSubCategoriaTela());
        }
        CategoriaTela categoriaTela = categoriaTelaRepository.findById(dto.getIdCategoriaTela())
                .orElseThrow(() -> new EntityNotFoundException(
                        "CategoriaTela con id " + dto.getIdCategoriaTela() + " no encontrada"));

        SubCategoriaTela nueva = SubCategoriaTela.builder()
                .codigoSubCategoriaTela(dto.getCodigoSubCategoriaTela())
                .nombreSubCategoriaTela(dto.getNombreSubCategoriaTela())
                .categoriaTela(categoriaTela)
                .build();
        return mapper.toDTO(subCategoriaTelaRepository.save(nueva));
    }

    @Override
    public SubCategoriaTelaDTO actualizar(Integer id, SubCategoriaTelaDTO dto) {
        SubCategoriaTela existente = findOrThrow(id);
        CategoriaTela categoriaTela = categoriaTelaRepository.findById(dto.getIdCategoriaTela())
                .orElseThrow(() -> new EntityNotFoundException(
                        "CategoriaTela con id " + dto.getIdCategoriaTela() + " no encontrada"));
        existente.setCodigoSubCategoriaTela(dto.getCodigoSubCategoriaTela());
        existente.setNombreSubCategoriaTela(dto.getNombreSubCategoriaTela());
        existente.setCategoriaTela(categoriaTela);
        return mapper.toDTO(subCategoriaTelaRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public SubCategoriaTelaDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoriaTelaDTO> listarTodas() {
        return subCategoriaTelaRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoriaTelaDTO> listarPorCategoriaTela(Integer idCategoriaTela) {
        return subCategoriaTelaRepository.findByCategoriaTelajId(idCategoriaTela)
                .stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!subCategoriaTelaRepository.existsById(id)) {
            throw new EntityNotFoundException("SubCategoriaTela con id " + id + " no encontrada");
        }
        subCategoriaTelaRepository.deleteById(id);
    }

    private SubCategoriaTela findOrThrow(Integer id) {
        return subCategoriaTelaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubCategoriaTela con id " + id + " no encontrada"));
    }
}
