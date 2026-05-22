package backend.com.shared.application.service;

import backend.com.shared.application.dto.TipoCuentaBancariaRequest;
import backend.com.shared.application.dto.TipoCuentaBancariaResponse;
import backend.com.shared.domain.model.TipoCuentaBancaria;
import backend.com.shared.domain.repository.TipoCuentaBancariaRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.TipoCuentaBancariaNotFoundException;
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

    @Override
    @Transactional(readOnly = true)
    public List<TipoCuentaBancariaResponse> listarTodos() {
        return tipoCuentaBancariaRepository.findAll().stream()
                .map(TipoCuentaBancariaResponse::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoCuentaBancariaResponse> obtenerPorId(Integer id) {
        return tipoCuentaBancariaRepository.findById(id).map(TipoCuentaBancariaResponse::fromDomain);
    }

    @Override
    public TipoCuentaBancariaResponse crear(TipoCuentaBancariaRequest request) {
        String denominacion = request.getDenominacionCuenta().trim();

        boolean duplicado = tipoCuentaBancariaRepository.findAll().stream()
                .anyMatch(t -> t.getDenominacionCuenta().equalsIgnoreCase(denominacion));
        if (duplicado) {
            throw new BusinessRuleException(
                    "Ya existe un tipo de cuenta bancaria con la denominación: " + denominacion);
        }

        TipoCuentaBancaria tipo = TipoCuentaBancaria.builder()
                .denominacionCuenta(denominacion)
                .build();

        return TipoCuentaBancariaResponse.fromDomain(tipoCuentaBancariaRepository.save(tipo));
    }

    @Override
    public TipoCuentaBancariaResponse actualizar(Integer id, TipoCuentaBancariaRequest request) {
        TipoCuentaBancaria existente = tipoCuentaBancariaRepository.findById(id)
                .orElseThrow(() -> new TipoCuentaBancariaNotFoundException(id));

        String denominacion = request.getDenominacionCuenta().trim();

        boolean duplicado = tipoCuentaBancariaRepository.findAll().stream()
                .anyMatch(t -> t.getDenominacionCuenta().equalsIgnoreCase(denominacion)
                        && !t.getTipoCuentaId().equals(id));
        if (duplicado) {
            throw new BusinessRuleException(
                    "Ya existe un tipo de cuenta bancaria con la denominación: " + denominacion);
        }

        existente.setDenominacionCuenta(denominacion);

        return TipoCuentaBancariaResponse.fromDomain(tipoCuentaBancariaRepository.save(existente));
    }

    @Override
    public void eliminar(Integer id) {
        if (!tipoCuentaBancariaRepository.existsById(id)) {
            throw new TipoCuentaBancariaNotFoundException(id);
        }
        tipoCuentaBancariaRepository.deleteById(id);
    }
}
