package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.AtributoTecnicoDTO;
import backend.com.shared.application.service.AtributoTecnicoService;
import backend.com.shared.application.service.CodigoGeneratorService;
import backend.com.shared.domain.model.AtributoTecnico;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.AtributoTecnicoMapper;
import backend.com.shared.infrastructure.persistence.repository.AtributoTecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AtributoTecnicoServiceImpl implements AtributoTecnicoService {

    private static final String PREFIJO_CODIGO = "A-";

    private final AtributoTecnicoRepository atributoRepository;
    private final AtributoTecnicoMapper mapper;
    private final CodigoGeneratorService codigoGeneratorService;

    @Override
    public AtributoTecnicoDTO crear(AtributoTecnicoDTO dto) {
        String codigo = codigoGeneratorService.generarPorAbreviatura(
                PREFIJO_CODIGO, dto.getDescripcionTecnica(), atributoRepository::existsByCodigoAtributo);
        AtributoTecnico nuevo = AtributoTecnico.builder()
                .codigoAtributo(codigo)
                .clasificacion(dto.getClasificacion())
                .descripcionTecnica(dto.getDescripcionTecnica())
                .impactoErp(dto.getImpactoErp())
                .build();
        return mapper.toDTO(atributoRepository.save(nuevo));
    }

    @Override
    public AtributoTecnicoDTO actualizar(Integer id, AtributoTecnicoDTO dto) {
        AtributoTecnico existente = findOrThrow(id);
        existente.setCodigoAtributo(dto.getCodigoAtributo());
        existente.setClasificacion(dto.getClasificacion());
        existente.setDescripcionTecnica(dto.getDescripcionTecnica());
        existente.setImpactoErp(dto.getImpactoErp());
        return mapper.toDTO(atributoRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public AtributoTecnicoDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtributoTecnicoDTO> listarTodos() {
        return atributoRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtributoTecnicoDTO> listarPorClasificacion(String clasificacion) {
        return atributoRepository.findByClasificacionIgnoreCase(clasificacion)
                .stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!atributoRepository.existsById(id)) {
            throw new EntityNotFoundException("AtributoTecnico con id " + id + " no encontrado");
        }
        atributoRepository.deleteById(id);
    }

    private AtributoTecnico findOrThrow(Integer id) {
        return atributoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AtributoTecnico con id " + id + " no encontrado"));
    }
}