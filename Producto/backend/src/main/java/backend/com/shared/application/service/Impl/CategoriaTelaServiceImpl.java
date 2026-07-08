package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.CategoriaTelaDTO;
import backend.com.shared.application.service.CategoriaTelaService;
import backend.com.shared.application.service.CodigoGeneratorService;
import backend.com.shared.domain.model.CategoriaTela;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.CategoriaTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.CategoriaTelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaTelaServiceImpl implements CategoriaTelaService {

    private static final String PREFIJO_CODIGO = "CAT-";

    private final CategoriaTelaRepository categoriaTelaRepository;
    private final CategoriaTelaMapper mapper;
    private final CodigoGeneratorService codigoGeneratorService;

    @Override
    public CategoriaTelaDTO crear(CategoriaTelaDTO dto) {
        if (categoriaTelaRepository.existsByNombreCategoriaTela(dto.getNombreCategoriaTela())) {
            throw new DuplicadoException("nombre de categoría tela", dto.getNombreCategoriaTela());
        }
        String codigo = codigoGeneratorService.generarPorAbreviatura(
                PREFIJO_CODIGO, dto.getNombreCategoriaTela(), categoriaTelaRepository::existsByCodigoCategoriaTela);
        CategoriaTela nueva = CategoriaTela.builder()
                .codigoCategoriaTela(codigo)
                .nombreCategoriaTela(dto.getNombreCategoriaTela())
                .build();
        return mapper.toDTO(categoriaTelaRepository.save(nueva));
    }

    @Override
    public CategoriaTelaDTO actualizar(Integer id, CategoriaTelaDTO dto) {
        CategoriaTela existente = findOrThrow(id);

        if (!existente.getCodigoCategoriaTela().equals(dto.getCodigoCategoriaTela())
                && categoriaTelaRepository.existsByCodigoCategoriaTela(dto.getCodigoCategoriaTela())) {
            throw new DuplicadoException("código de categoría tela", dto.getCodigoCategoriaTela());
        }
        if (!existente.getNombreCategoriaTela().equals(dto.getNombreCategoriaTela())
                && categoriaTelaRepository.existsByNombreCategoriaTela(dto.getNombreCategoriaTela())) {
            throw new DuplicadoException("nombre de categoría tela", dto.getNombreCategoriaTela());
        }

        existente.setCodigoCategoriaTela(dto.getCodigoCategoriaTela());
        existente.setNombreCategoriaTela(dto.getNombreCategoriaTela());
        return mapper.toDTO(categoriaTelaRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaTelaDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaTelaDTO> listarTodas() {
        return categoriaTelaRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!categoriaTelaRepository.existsById(id)) {
            throw new EntityNotFoundException("CategoriaTela con id " + id + " no encontrada");
        }
        categoriaTelaRepository.deleteById(id);
    }

    private CategoriaTela findOrThrow(Integer id) {
        return categoriaTelaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CategoriaTela con id " + id + " no encontrada"));
    }
}
