package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.catalogo.AtributoTecnico;
import backend.com.maestros.dto.request.AtributoTecnicoRequestDTO;
import backend.com.maestros.dto.response.AtributoTecnicoResponseDTO;
import backend.com.maestros.exception.DuplicadoException;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.AtributoTecnicoRepository;
import backend.com.maestros.service.AtributoTecnicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AtributoTecnicoServiceImpl implements AtributoTecnicoService {

    private final AtributoTecnicoRepository atributoRepository;

    @Override
    public AtributoTecnicoResponseDTO crear(AtributoTecnicoRequestDTO request) {
        if (atributoRepository.existsByCodigoAtributo(request.getCodigoAtributo())) {
            throw new DuplicadoException("código de atributo", request.getCodigoAtributo());
        }
        return toResponse(atributoRepository.save(
                AtributoTecnico.builder()
                        .codigoAtributo(request.getCodigoAtributo())
                        .clasificacion(request.getClasificacion())
                        .descripcionTecnica(request.getDescripcionTecnica())
                        .impactoErp(request.getImpactoErp())
                        .build()));
    }

    @Override
    public AtributoTecnicoResponseDTO actualizar(Integer id, AtributoTecnicoRequestDTO request) {
        AtributoTecnico a = findOrThrow(id);
        a.setCodigoAtributo(request.getCodigoAtributo());
        a.setClasificacion(request.getClasificacion());
        a.setDescripcionTecnica(request.getDescripcionTecnica());
        a.setImpactoErp(request.getImpactoErp());
        return toResponse(atributoRepository.save(a));
    }

    @Override @Transactional(readOnly = true)
    public AtributoTecnicoResponseDTO obtenerPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Override @Transactional(readOnly = true)
    public List<AtributoTecnicoResponseDTO> listarTodos() {
        return atributoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<AtributoTecnicoResponseDTO> listarPorClasificacion(String clasificacion) {
        return atributoRepository.findByClasificacionIgnoreCase(clasificacion)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public void eliminar(Integer id) {
        findOrThrow(id);
        atributoRepository.deleteById(id);
    }

    private AtributoTecnico findOrThrow(Integer id) {
        return atributoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("AtributoTecnico", id));
    }

    private AtributoTecnicoResponseDTO toResponse(AtributoTecnico a) {
        return AtributoTecnicoResponseDTO.builder()
                .idAtributo(a.getIdAtributo())
                .codigoAtributo(a.getCodigoAtributo())
                .clasificacion(a.getClasificacion())
                .descripcionTecnica(a.getDescripcionTecnica())
                .impactoErp(a.getImpactoErp())
                .build();
    }
}
