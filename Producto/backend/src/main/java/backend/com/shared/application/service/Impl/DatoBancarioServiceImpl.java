package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.DatoBancarioDTO;
import backend.com.shared.application.service.DatoBancarioService;
import backend.com.shared.domain.model.Banco;
import backend.com.shared.domain.model.DatoBancario;
import backend.com.shared.domain.model.TipoCuentaBancaria;
import backend.com.shared.exception.BancoNotFoundException;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.DatoBancarioNotFoundException;
import backend.com.shared.exception.TipoCuentaBancariaNotFoundException;
import backend.com.shared.infrastructure.mapper.DatoBancarioMapper;
import backend.com.shared.infrastructure.persistence.repository.BancoRepository;
import backend.com.shared.infrastructure.persistence.repository.DatoBancarioRepository;
import backend.com.shared.infrastructure.persistence.repository.TipoCuentaBancariaRepository;
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
    private final DatoBancarioMapper datoBancarioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DatoBancarioDTO> listarTodos() {
        return datoBancarioRepository.findAll().stream()
                .map(datoBancarioMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DatoBancarioDTO> obtenerPorId(Integer id) {
        return datoBancarioRepository.findById(id).map(datoBancarioMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DatoBancarioDTO> listarPorBanco(Integer bancoId) {
        if (!bancoRepository.existsById(bancoId)) {
            throw new BancoNotFoundException(bancoId);
        }
        return datoBancarioRepository.findByBancoId(bancoId).stream()
                .map(datoBancarioMapper::toDTO)
                .toList();
    }

    @Override
    public DatoBancarioDTO crear(DatoBancarioDTO dto) {
        Integer bancoId = extraerBancoId(dto);
        Integer tipoCuentaId = extraerTipoCuentaId(dto);

        Banco banco = bancoRepository.findById(bancoId)
                .orElseThrow(() -> new BancoNotFoundException(bancoId));

        TipoCuentaBancaria tipoCuenta = tipoCuentaBancariaRepository.findById(tipoCuentaId)
                .orElseThrow(() -> new TipoCuentaBancariaNotFoundException(tipoCuentaId));

        DatoBancario dato = DatoBancario.builder()
                .numeroCuenta(dto.getNumeroCuenta().trim())
                .banco(banco)
                .tipoCuentaBancaria(tipoCuenta)
                .build();

        return datoBancarioMapper.toDTO(datoBancarioRepository.save(dato));
    }

    @Override
    public DatoBancarioDTO actualizar(Integer id, DatoBancarioDTO dto) {
        DatoBancario existente = datoBancarioRepository.findById(id)
                .orElseThrow(() -> new DatoBancarioNotFoundException(id));

        Integer bancoId = extraerBancoId(dto);
        Integer tipoCuentaId = extraerTipoCuentaId(dto);

        Banco banco = bancoRepository.findById(bancoId)
                .orElseThrow(() -> new BancoNotFoundException(bancoId));

        TipoCuentaBancaria tipoCuenta = tipoCuentaBancariaRepository.findById(tipoCuentaId)
                .orElseThrow(() -> new TipoCuentaBancariaNotFoundException(tipoCuentaId));

        existente.setNumeroCuenta(dto.getNumeroCuenta().trim());
        existente.setBanco(banco);
        existente.setTipoCuentaBancaria(tipoCuenta);

        return datoBancarioMapper.toDTO(datoBancarioRepository.save(existente));
    }

    @Override
    public void eliminar(Integer id) {
        if (!datoBancarioRepository.existsById(id)) {
            throw new DatoBancarioNotFoundException(id);
        }
        datoBancarioRepository.deleteById(id);
    }

    private Integer extraerBancoId(DatoBancarioDTO dto) {
        if (dto.getBanco() == null || dto.getBanco().getBancoId() == null) {
            throw new BusinessRuleException("El banco del dato bancario es obligatorio");
        }
        return dto.getBanco().getBancoId();
    }

    private Integer extraerTipoCuentaId(DatoBancarioDTO dto) {
        if (dto.getTipoCuentaBancaria() == null || dto.getTipoCuentaBancaria().getTipoCuentaId() == null) {
            throw new BusinessRuleException("El tipo de cuenta del dato bancario es obligatorio");
        }
        return dto.getTipoCuentaBancaria().getTipoCuentaId();
    }
}
