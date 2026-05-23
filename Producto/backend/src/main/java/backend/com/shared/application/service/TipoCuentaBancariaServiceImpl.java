package backend.com.shared.application.service;

import backend.com.shared.application.dto.TipoCuentaBancariaDTO;
import backend.com.shared.domain.model.TipoCuentaBancaria;
import backend.com.shared.domain.repository.TipoCuentaBancariaRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.TipoCuentaBancariaNotFoundException;
import backend.com.shared.infrastructure.mapper.TipoCuentaBancariaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoCuentaBancariaServiceImpl implements TipoCuentaBancariaService {

    private final TipoCuentaBancariaRepository tipoCuentaBancariaRepository;
    private final TipoCuentaBancariaMapper tipoCuentaBancariaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TipoCuentaBancariaDTO> listarTodos() {
        return tipoCuentaBancariaRepository.findAll().stream()
                .map(tipoCuentaBancariaMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoCuentaBancariaDTO> obtenerPorId(Integer id) {
        return tipoCuentaBancariaRepository.findById(id).map(tipoCuentaBancariaMapper::toDTO);
    }

    @Override
    public TipoCuentaBancariaDTO crear(TipoCuentaBancariaDTO dto) {
        String denominacion = dto.getDenominacionCuenta().trim();

        boolean duplicado = tipoCuentaBancariaRepository.findAll().stream()
                .anyMatch(t -> t.getDenominacionCuenta().equalsIgnoreCase(denominacion));
        if (duplicado) {
            throw new BusinessRuleException(
                    "Ya existe un tipo de cuenta bancaria con la denominación: " + denominacion);
        }

        TipoCuentaBancaria tipo = TipoCuentaBancaria.builder()
                .denominacionCuenta(denominacion)
                .build();

        return tipoCuentaBancariaMapper.toDTO(tipoCuentaBancariaRepository.save(tipo));
    }

    @Override
    public TipoCuentaBancariaDTO actualizar(Integer id, TipoCuentaBancariaDTO dto) {
        TipoCuentaBancaria existente = tipoCuentaBancariaRepository.findById(id)
                .orElseThrow(() -> new TipoCuentaBancariaNotFoundException(id));

        String denominacion = dto.getDenominacionCuenta().trim();

        boolean duplicado = tipoCuentaBancariaRepository.findAll().stream()
                .anyMatch(t -> t.getDenominacionCuenta().equalsIgnoreCase(denominacion)
                        && !t.getTipoCuentaId().equals(id));
        if (duplicado) {
            throw new BusinessRuleException(
                    "Ya existe un tipo de cuenta bancaria con la denominación: " + denominacion);
        }

        existente.setDenominacionCuenta(denominacion);

        return tipoCuentaBancariaMapper.toDTO(tipoCuentaBancariaRepository.save(existente));
    }

    @Override
    public void eliminar(Integer id) {
        if (!tipoCuentaBancariaRepository.existsById(id)) {
            throw new TipoCuentaBancariaNotFoundException(id);
        }
        tipoCuentaBancariaRepository.deleteById(id);
    }
}