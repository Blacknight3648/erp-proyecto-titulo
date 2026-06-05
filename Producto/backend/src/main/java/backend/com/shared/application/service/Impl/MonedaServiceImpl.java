package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.MonedaDTO;
import backend.com.shared.application.service.MonedaService;
import backend.com.shared.domain.model.Moneda;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.MonedaMapper;
import backend.com.shared.infrastructure.persistence.repository.MonedaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MonedaServiceImpl implements MonedaService {

    private final MonedaRepository monedaRepository;
    private final MonedaMapper monedaMapper;

    @Override
    public MonedaDTO crear(MonedaDTO dto) {
        if (monedaRepository.existsByCodigoMoneda(dto.getCodigoMoneda())) {
            throw new DuplicadoException("código ISO de moneda", dto.getCodigoMoneda());
        }
        Moneda nueva = Moneda.builder()
                .codigoMoneda(dto.getCodigoMoneda())
                .nombreMoneda(dto.getNombreMoneda())
                .simbolo(dto.getSimbolo())
                .build();
        return monedaMapper.toDTO(monedaRepository.save(nueva));
    }

    @Override
    public MonedaDTO actualizar(Integer id, MonedaDTO dto) {
        Moneda existente = findOrThrow(id);
        existente.setCodigoMoneda(dto.getCodigoMoneda());
        existente.setNombreMoneda(dto.getNombreMoneda());
        existente.setSimbolo(dto.getSimbolo());
        return monedaMapper.toDTO(monedaRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public MonedaDTO obtenerPorId(Integer id) {
        return monedaMapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonedaDTO> listarTodas() {
        return monedaRepository.findAll().stream()
                .map(monedaMapper::toDTO)
                .toList();
    }

    @Override
    public void eliminar(Integer id) {
        if (!monedaRepository.existsById(id)) {
            throw new EntityNotFoundException("Moneda con id " + id + " no encontrada");
        }
        monedaRepository.deleteById(id);
    }

    private Moneda findOrThrow(Integer id) {
        return monedaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moneda con id " + id + " no encontrada"));
    }
}