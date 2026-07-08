package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.AtributoAccesorioDefinicionDTO;
import backend.com.shared.application.service.AtributoAccesorioDefinicionService;
import backend.com.shared.domain.model.AtributoAccesorioDefinicion;
import backend.com.shared.domain.model.TipoAccesorio;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.AtributoAccesorioDefinicionMapper;
import backend.com.shared.infrastructure.persistence.repository.AtributoAccesorioDefinicionRepository;
import backend.com.shared.infrastructure.persistence.repository.TipoAccesorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AtributoAccesorioDefinicionServiceImpl implements AtributoAccesorioDefinicionService {

    private final AtributoAccesorioDefinicionRepository definicionRepository;
    private final TipoAccesorioRepository tipoAccesorioRepository;
    private final AtributoAccesorioDefinicionMapper mapper;

    @Override
    public AtributoAccesorioDefinicionDTO crear(AtributoAccesorioDefinicionDTO dto) {
        AtributoAccesorioDefinicion nueva = mapper.toDomain(dto);
        nueva.setTipoAccesorio(resolverTipoAccesorio(dto.getTipoAccesorio().getIdTipoAccesorio()));
        if (nueva.getOrden() == null) nueva.setOrden(0);
        if (nueva.getRequerido() == null) nueva.setRequerido(false);
        return mapper.toDTO(definicionRepository.save(nueva));
    }

    @Override
    public AtributoAccesorioDefinicionDTO actualizar(Integer id, AtributoAccesorioDefinicionDTO dto) {
        AtributoAccesorioDefinicion existente = findOrThrow(id);
        existente.setNombreCampo(dto.getNombreCampo());
        existente.setTipoDato(dto.getTipoDato());
        existente.setOpciones(mapper.toDomain(dto).getOpciones());
        existente.setOrden(dto.getOrden() != null ? dto.getOrden() : 0);
        existente.setRequerido(dto.getRequerido() != null ? dto.getRequerido() : false);
        if (dto.getTipoAccesorio() != null && dto.getTipoAccesorio().getIdTipoAccesorio() != null) {
            existente.setTipoAccesorio(resolverTipoAccesorio(dto.getTipoAccesorio().getIdTipoAccesorio()));
        }
        return mapper.toDTO(definicionRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public AtributoAccesorioDefinicionDTO obtenerPorId(Integer id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtributoAccesorioDefinicionDTO> listarTodos() {
        return definicionRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtributoAccesorioDefinicionDTO> listarPorTipoAccesorio(Integer idTipoAccesorio) {
        return definicionRepository.findByTipoAccesorioIdOrderByOrdenAsc(idTipoAccesorio)
                .stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!definicionRepository.existsById(id)) {
            throw new EntityNotFoundException("AtributoAccesorioDefinicion con id " + id + " no encontrada");
        }
        definicionRepository.deleteById(id);
    }

    private TipoAccesorio resolverTipoAccesorio(Integer id) {
        return tipoAccesorioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TipoAccesorio con id " + id + " no encontrado"));
    }

    private AtributoAccesorioDefinicion findOrThrow(Integer id) {
        return definicionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "AtributoAccesorioDefinicion con id " + id + " no encontrada"));
    }
}
