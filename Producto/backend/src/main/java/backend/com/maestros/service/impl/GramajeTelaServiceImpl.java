package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.catalogo.GramajeTela;
import backend.com.maestros.dto.request.GramajeTelaRequestDTO;
import backend.com.maestros.dto.response.GramajeTelaResponseDTO;
import backend.com.maestros.exception.DuplicadoException;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.GramajeTelaRepository;
import backend.com.maestros.service.GramajeTelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GramajeTelaServiceImpl implements GramajeTelaService {

    private final GramajeTelaRepository gramajeRepository;

    @Override
    public GramajeTelaResponseDTO crear(GramajeTelaRequestDTO request) {
        if (gramajeRepository.existsByCodigoGramaje(request.getCodigoGramaje())) {
            throw new DuplicadoException("código de gramaje", request.getCodigoGramaje());
        }
        return toResponse(gramajeRepository.save(
                GramajeTela.builder()
                        .codigoGramaje(request.getCodigoGramaje())
                        .valorGramosM2(request.getValorGramosM2())
                        .categoriaVestuario(request.getCategoriaVestuario())
                        .build()));
    }

    @Override
    public GramajeTelaResponseDTO actualizar(Integer id, GramajeTelaRequestDTO request) {
        GramajeTela g = findOrThrow(id);
        g.setCodigoGramaje(request.getCodigoGramaje());
        g.setValorGramosM2(request.getValorGramosM2());
        g.setCategoriaVestuario(request.getCategoriaVestuario());
        return toResponse(gramajeRepository.save(g));
    }

    @Override @Transactional(readOnly = true)
    public GramajeTelaResponseDTO obtenerPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Override @Transactional(readOnly = true)
    public List<GramajeTelaResponseDTO> listarTodos() {
        return gramajeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<GramajeTelaResponseDTO> listarPorCategoriaVestuario(String categoria) {
        return gramajeRepository.findByCategoriaVestuarioIgnoreCase(categoria)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public void eliminar(Integer id) {
        findOrThrow(id);
        gramajeRepository.deleteById(id);
    }

    private GramajeTela findOrThrow(Integer id) {
        return gramajeRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("GramajeTela", id));
    }

    private GramajeTelaResponseDTO toResponse(GramajeTela g) {
        return GramajeTelaResponseDTO.builder()
                .idGramaje(g.getIdGramaje())
                .codigoGramaje(g.getCodigoGramaje())
                .valorGramosM2(g.getValorGramosM2())
                .categoriaVestuario(g.getCategoriaVestuario())
                .build();
    }
}
