package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.CategoriaDTO;
import backend.com.shared.application.service.CategoriaService;
import backend.com.shared.domain.model.Categoria;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.CategoriaMapper;
import backend.com.shared.infrastructure.persistence.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper mapper;

    @Override
    public CategoriaDTO crear(CategoriaDTO dto) {
        if (categoriaRepository.existsByCodigoCategoria(dto.getCodigoCategoria())) {
            throw new DuplicadoException("código de categoría", dto.getCodigoCategoria());
        }
        if (categoriaRepository.existsByNombreCategoria(dto.getNombreCategoria())) {
            throw new DuplicadoException("nombre de categoría", dto.getNombreCategoria());
        }
        Categoria nueva = Categoria.builder()
                .codigoCategoria(dto.getCodigoCategoria())
                .nombreCategoria(dto.getNombreCategoria())
                .build();
        return mapper.toDTO(categoriaRepository.save(nueva));
    }

    @Override
    public CategoriaDTO actualizar(Integer id, CategoriaDTO dto) {
        Categoria existente = findOrThrow(id);

        if (!existente.getCodigoCategoria().equals(dto.getCodigoCategoria())
                && categoriaRepository.existsByCodigoCategoria(dto.getCodigoCategoria())) {
            throw new DuplicadoException("código de categoría", dto.getCodigoCategoria());
        }
        if (!existente.getNombreCategoria().equals(dto.getNombreCategoria())
                && categoriaRepository.existsByNombreCategoria(dto.getNombreCategoria())) {
            throw new DuplicadoException("nombre de categoría", dto.getNombreCategoria());
        }

        existente.setCodigoCategoria(dto.getCodigoCategoria());
        existente.setNombreCategoria(dto.getNombreCategoria());
        return mapper.toDTO(categoriaRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoría con id " + id + " no encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    private Categoria findOrThrow(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría con id " + id + " no encontrada"));
    }
}