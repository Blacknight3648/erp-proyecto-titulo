package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.GramajeTelaDTO;
import backend.com.shared.application.service.CodigoGeneratorService;
import backend.com.shared.application.service.GramajeTelaService;
import backend.com.shared.domain.model.GramajeTela;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.GramajeTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.GramajeTelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GramajeTelaServiceImpl implements GramajeTelaService {

    private static final String PREFIJO_CODIGO = "G-";

    private final GramajeTelaRepository gramajeRepository;
    private final GramajeTelaMapper mapper;
    private final CodigoGeneratorService codigoGeneratorService;

    @Override
    public GramajeTelaDTO crear(GramajeTelaDTO dto) {
        String codigo = codigoGeneratorService.generarGramaje(
                PREFIJO_CODIGO, dto.getValorGramosM2(), gramajeRepository::existsByCodigoGramaje);
        GramajeTela nuevo = GramajeTela.builder()
                .codigoGramaje(codigo)
                .valorGramosM2(dto.getValorGramosM2())
                .categoriaVestuario(dto.getCategoriaVestuario())
                .build();
        return mapper.toDTO(gramajeRepository.save(nuevo));
    }

    @Override
    public GramajeTelaDTO actualizar(Integer id, GramajeTelaDTO dto) {
        GramajeTela existente = findOrThrow(id);
        existente.setCodigoGramaje(dto.getCodigoGramaje());
        existente.setValorGramosM2(dto.getValorGramosM2());
        existente.setCategoriaVestuario(dto.getCategoriaVestuario());
        return mapper.toDTO(gramajeRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public GramajeTelaDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GramajeTelaDTO> listarTodos() {
        return gramajeRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GramajeTelaDTO> listarPorCategoriaVestuario(String categoria) {
        return gramajeRepository.findByCategoriaVestuarioIgnoreCase(categoria)
                .stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!gramajeRepository.existsById(id)) {
            throw new EntityNotFoundException("GramajeTela con id " + id + " no encontrada");
        }
        gramajeRepository.deleteById(id);
    }

    private GramajeTela findOrThrow(Integer id) {
        return gramajeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("GramajeTela con id " + id + " no encontrada"));
    }
}