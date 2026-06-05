package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.SubCategoriaDTO;
import backend.com.shared.application.service.SubCategoriaService;
import backend.com.shared.domain.model.Categoria;
import backend.com.shared.domain.model.SubCategoria;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.SubCategoriaMapper;
import backend.com.shared.infrastructure.persistence.repository.CategoriaRepository;
import backend.com.shared.infrastructure.persistence.repository.SubCategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubCategoriaServiceImpl implements SubCategoriaService {

    private final SubCategoriaRepository subCategoriaRepository;
    private final CategoriaRepository categoriaRepository;
    private final SubCategoriaMapper mapper;

    @Override
    public SubCategoriaDTO crear(SubCategoriaDTO dto) {
        if (subCategoriaRepository.existsByCodigoSubcategoria(dto.getCodigoSubcategoria())) {
            throw new DuplicadoException("código de subcategoría", dto.getCodigoSubcategoria());
        }
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoría con id " + dto.getIdCategoria() + " no encontrada"));

        SubCategoria nueva = SubCategoria.builder()
                .codigoSubcategoria(dto.getCodigoSubcategoria())
                .nombreSubCategoria(dto.getNombreSubCategoria())
                .categoria(categoria)
                .build();
        return mapper.toDTO(subCategoriaRepository.save(nueva));
    }

    @Override
    public SubCategoriaDTO actualizar(Integer id, SubCategoriaDTO dto) {
        SubCategoria existente = findOrThrow(id);
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoría con id " + dto.getIdCategoria() + " no encontrada"));
        existente.setCodigoSubcategoria(dto.getCodigoSubcategoria());
        existente.setNombreSubCategoria(dto.getNombreSubCategoria());
        existente.setCategoria(categoria);
        return mapper.toDTO(subCategoriaRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public SubCategoriaDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoriaDTO> listarTodas() {
        return subCategoriaRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoriaDTO> listarPorCategoria(Integer idCategoria) {
        return subCategoriaRepository.findByCategoriaId(idCategoria)
                .stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!subCategoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("SubCategoría con id " + id + " no encontrada");
        }
        subCategoriaRepository.deleteById(id);
    }

    private SubCategoria findOrThrow(Integer id) {
        return subCategoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubCategoría con id " + id + " no encontrada"));
    }
}