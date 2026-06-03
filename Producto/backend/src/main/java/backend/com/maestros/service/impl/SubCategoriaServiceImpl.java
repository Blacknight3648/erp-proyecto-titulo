package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.Categoria;
import backend.com.maestros.domain.entity.SubCategoria;
import backend.com.maestros.dto.request.SubCategoriaRequestDTO;
import backend.com.maestros.dto.response.SubCategoriaResponseDTO;
import backend.com.maestros.exception.DuplicadoException;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.CategoriaRepository;
import backend.com.maestros.repository.SubCategoriaRepository;
import backend.com.maestros.service.SubCategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubCategoriaServiceImpl implements SubCategoriaService {

    private final SubCategoriaRepository subCategoriaRepository;
    private final CategoriaRepository    categoriaRepository;

    @Override
    public SubCategoriaResponseDTO crear(SubCategoriaRequestDTO request) {
        if (subCategoriaRepository.existsByCodigoSubcategoria(request.getCodigoSubcategoria())) {
            throw new DuplicadoException("código de subcategoría", request.getCodigoSubcategoria());
        }
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", request.getIdCategoria()));

        SubCategoria saved = subCategoriaRepository.save(
                SubCategoria.builder()
                        .codigoSubcategoria(request.getCodigoSubcategoria())
                        .nombreSubCategoria(request.getNombreSubCategoria())
                        .categoria(categoria)
                        .build());
        return toResponse(saved);
    }

    @Override
    public SubCategoriaResponseDTO actualizar(Integer id, SubCategoriaRequestDTO request) {
        SubCategoria sc = findOrThrow(id);
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", request.getIdCategoria()));
        sc.setCodigoSubcategoria(request.getCodigoSubcategoria());
        sc.setNombreSubCategoria(request.getNombreSubCategoria());
        sc.setCategoria(categoria);
        return toResponse(subCategoriaRepository.save(sc));
    }

    @Override
    @Transactional(readOnly = true)
    public SubCategoriaResponseDTO obtenerPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoriaResponseDTO> listarTodas() {
        return subCategoriaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoriaResponseDTO> listarPorCategoria(Integer idCategoria) {
        return subCategoriaRepository.findByCategoria_IdCategoria(idCategoria)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public void eliminar(Integer id) {
        findOrThrow(id);
        subCategoriaRepository.deleteById(id);
    }

    // ── helpers ──────────────────────────────────────────────

    private SubCategoria findOrThrow(Integer id) {
        return subCategoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("SubCategoría", id));
    }

    private SubCategoriaResponseDTO toResponse(SubCategoria sc) {
        return SubCategoriaResponseDTO.builder()
                .idSubCategoria(sc.getIdSubCategoria())
                .codigoSubcategoria(sc.getCodigoSubcategoria())
                .nombreSubCategoria(sc.getNombreSubCategoria())
                .idCategoria(sc.getCategoria().getIdCategoria())
                .codigoCategoria(sc.getCategoria().getCodigoCategoria())
                .nombreCategoria(sc.getCategoria().getNombreCategoria())
                .build();
    }
}
