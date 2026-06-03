package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.Moneda;
import backend.com.maestros.dto.request.MonedaRequestDTO;
import backend.com.maestros.dto.response.MonedaResponseDTO;
import backend.com.maestros.exception.DuplicadoException;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.MonedaRepository;
import backend.com.maestros.service.MonedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MonedaServiceImpl implements MonedaService {

    private final MonedaRepository monedaRepository;

    @Override
    public MonedaResponseDTO crear(MonedaRequestDTO request) {
        if (monedaRepository.existsByCodigoMoneda(request.getCodigoMoneda())) {
            throw new DuplicadoException("código ISO de moneda", request.getCodigoMoneda());
        }
        Moneda saved = monedaRepository.save(
                Moneda.builder()
                        .codigoMoneda(request.getCodigoMoneda())
                        .nombreMoneda(request.getNombreMoneda())
                        .simbolo(request.getSimbolo())
                        .build());
        return toResponse(saved);
    }

    @Override
    public MonedaResponseDTO actualizar(Integer id, MonedaRequestDTO request) {
        Moneda m = findOrThrow(id);
        m.setCodigoMoneda(request.getCodigoMoneda());
        m.setNombreMoneda(request.getNombreMoneda());
        m.setSimbolo(request.getSimbolo());
        return toResponse(monedaRepository.save(m));
    }

    @Override
    @Transactional(readOnly = true)
    public MonedaResponseDTO obtenerPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonedaResponseDTO> listarTodas() {
        return monedaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public void eliminar(Integer id) {
        findOrThrow(id);
        monedaRepository.deleteById(id);
    }

    private Moneda findOrThrow(Integer id) {
        return monedaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Moneda", id));
    }

    private MonedaResponseDTO toResponse(Moneda m) {
        return MonedaResponseDTO.builder()
                .idMoneda(m.getIdMoneda())
                .codigoMoneda(m.getCodigoMoneda())
                .nombreMoneda(m.getNombreMoneda())
                .simbolo(m.getSimbolo())
                .build();
    }
}
