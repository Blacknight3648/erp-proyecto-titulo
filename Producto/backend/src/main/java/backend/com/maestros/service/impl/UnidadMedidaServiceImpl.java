package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.UnidadMedida;
import backend.com.maestros.dto.request.UnidadMedidaRequestDTO;
import backend.com.maestros.dto.response.UnidadMedidaResponseDTO;
import backend.com.maestros.exception.DuplicadoException;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.UnidadMedidaRepository;
import backend.com.maestros.service.UnidadMedidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UnidadMedidaServiceImpl implements UnidadMedidaService {

    private final UnidadMedidaRepository unidadMedidaRepository;

    @Override
    public UnidadMedidaResponseDTO crear(UnidadMedidaRequestDTO request) {
        if (unidadMedidaRepository.existsByAbreviatura(request.getAbreviatura())) {
            throw new DuplicadoException("abreviatura", request.getAbreviatura());
        }
        UnidadMedida saved = unidadMedidaRepository.save(
                UnidadMedida.builder()
                        .nombreUnidad(request.getNombreUnidad())
                        .abreviatura(request.getAbreviatura())
                        .build());
        return toResponse(saved);
    }

    @Override
    public UnidadMedidaResponseDTO actualizar(Integer id, UnidadMedidaRequestDTO request) {
        UnidadMedida u = findOrThrow(id);
        u.setNombreUnidad(request.getNombreUnidad());
        u.setAbreviatura(request.getAbreviatura());
        return toResponse(unidadMedidaRepository.save(u));
    }

    @Override
    @Transactional(readOnly = true)
    public UnidadMedidaResponseDTO obtenerPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadMedidaResponseDTO> listarTodas() {
        return unidadMedidaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public void eliminar(Integer id) {
        findOrThrow(id);
        unidadMedidaRepository.deleteById(id);
    }

    private UnidadMedida findOrThrow(Integer id) {
        return unidadMedidaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("UnidadMedida", id));
    }

    private UnidadMedidaResponseDTO toResponse(UnidadMedida u) {
        return UnidadMedidaResponseDTO.builder()
                .idUnidadMedida(u.getIdUnidadMedida())
                .nombreUnidad(u.getNombreUnidad())
                .abreviatura(u.getAbreviatura())
                .build();
    }
}
