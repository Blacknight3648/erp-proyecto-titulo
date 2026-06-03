package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.Categoria;
import backend.com.maestros.dto.request.CategoriaRequestDTO;
import backend.com.maestros.dto.response.CategoriaResponseDTO;
import backend.com.maestros.exception.DuplicadoException;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.CategoriaRepository;
import backend.com.maestros.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public CategoriaResponseDTO crear(CategoriaRequestDTO request) {
        if (categoriaRepository.existsByCodigoCategoria(request.getCodigoCategoria())) {
            throw new DuplicadoException("código de categoría", request.getCodigoCategoria());
        }
        if (categoriaRepository.existsByNombreCategoria(request.getNombreCategoria())) {
            throw new DuplicadoException("nombre de categoría", request.getNombreCategoria());
        }
        Categoria saved = categoriaRepository.save(
                Categoria.builder()
                        .codigoCategoria(request.getCodigoCategoria())
                        .nombreCategoria(request.getNombreCategoria())
                        .build());
        return toResponse(saved);
    }

    @Override
    public CategoriaResponseDTO actualizar(Integer id, CategoriaRequestDTO request) {
        Categoria categoria = findOrThrow(id);

        if (!categoria.getCodigoCategoria().equals(request.getCodigoCategoria())
                && categoriaRepository.existsByCodigoCategoria(request.getCodigoCategoria())) {
            throw new DuplicadoException("código de categoría", request.getCodigoCategoria());
        }
        if (!categoria.getNombreCategoria().equals(request.getNombreCategoria())
                && categoriaRepository.existsByNombreCategoria(request.getNombreCategoria())) {
            throw new DuplicadoException("nombre de categoría", request.getNombreCategoria());
        }

        categoria.setCodigoCategoria(request.getCodigoCategoria());
        categoria.setNombreCategoria(request.getNombreCategoria());
        return toResponse(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO obtenerPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public void eliminar(Integer id) {
        findOrThrow(id);
        categoriaRepository.deleteById(id);
    }

    // ── helpers ──────────────────────────────────────────────

    private Categoria findOrThrow(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));
    }

    private CategoriaResponseDTO toResponse(Categoria c) {
        return CategoriaResponseDTO.builder()
                .idCategoria(c.getIdCategoria())
                .codigoCategoria(c.getCodigoCategoria())
                .nombreCategoria(c.getNombreCategoria())
                .build();
    }
}
