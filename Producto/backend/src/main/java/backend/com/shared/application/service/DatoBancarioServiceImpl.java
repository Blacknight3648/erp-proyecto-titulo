package backend.com.shared.application.service;

import backend.com.shared.application.dto.DatoBancarioRequest;
import backend.com.shared.application.dto.DatoBancarioResponse;
import backend.com.shared.domain.model.Banco;
import backend.com.shared.domain.model.DatoBancario;
import backend.com.shared.domain.model.TipoCuentaBancaria;
import backend.com.shared.domain.repository.BancoRepository;
import backend.com.shared.domain.repository.DatoBancarioRepository;
import backend.com.shared.domain.repository.TipoCuentaBancariaRepository;
import backend.com.shared.exception.BancoNotFoundException;
import backend.com.shared.exception.DatoBancarioNotFoundException;
import backend.com.shared.exception.TipoCuentaBancariaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DatoBancarioServiceImpl implements DatoBancarioService {

    private final DatoBancarioRepository datoBancarioRepository;
    private final BancoRepository bancoRepository;
    private final TipoCuentaBancariaRepository tipoCuentaBancariaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DatoBancarioResponse> listarTodos() {
        return datoBancarioRepository.findAll().stream()
                .map(DatoBancarioResponse::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DatoBancarioResponse> obtenerPorId(Integer id) {
        return datoBancarioRepository.findById(id).map(DatoBancarioResponse::fromDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DatoBancarioResponse> listarPorBanco(Integer bancoId) {
        if (!bancoRepository.existsById(bancoId)) {
            throw new BancoNotFoundException(bancoId);
        }
        return datoBancarioRepository.findByBancoId(bancoId).stream()
                .map(DatoBancarioResponse::fromDomain)
                .toList();
    }

    @Override
    public DatoBancarioResponse crear(DatoBancarioRequest request) {
        Banco banco = bancoRepository.findById(request.getBancoId())
                .orElseThrow(() -> new BancoNotFoundException(request.getBancoId()));

        TipoCuentaBancaria tipoCuenta = tipoCuentaBancariaRepository.findById(request.getTipoCuentaId())
                .orElseThrow(() -> new TipoCuentaBancariaNotFoundException(request.getTipoCuentaId()));

        DatoBancario dato = DatoBancario.builder()
                .numeroCuenta(request.getNumeroCuenta().trim())
                .banco(banco)
                .tipoCuentaBancaria(tipoCuenta)
                .build();

        return DatoBancarioResponse.fromDomain(datoBancarioRepository.save(dato));
    }

    @Override
    public DatoBancarioResponse actualizar(Integer id, DatoBancarioRequest request) {
        DatoBancario existente = datoBancarioRepository.findById(id)
                .orElseThrow(() -> new DatoBancarioNotFoundException(id));

        Banco banco = bancoRepository.findById(request.getBancoId())
                .orElseThrow(() -> new BancoNotFoundException(request.getBancoId()));

        TipoCuentaBancaria tipoCuenta = tipoCuentaBancariaRepository.findById(request.getTipoCuentaId())
                .orElseThrow(() -> new TipoCuentaBancariaNotFoundException(request.getTipoCuentaId()));

        existente.setNumeroCuenta(request.getNumeroCuenta().trim());
        existente.setBanco(banco);
        existente.setTipoCuentaBancaria(tipoCuenta);

        return DatoBancarioResponse.fromDomain(datoBancarioRepository.save(existente));
    }

    @Override
    public void eliminar(Integer id) {
        if (!datoBancarioRepository.existsById(id)) {
            throw new DatoBancarioNotFoundException(id);
        }
        datoBancarioRepository.deleteById(id);
    }
}
