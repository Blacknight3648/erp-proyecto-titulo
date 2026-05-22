package backend.com.shared.application.service;

import backend.com.shared.application.dto.BancoRequest;
import backend.com.shared.application.dto.BancoResponse;
import backend.com.shared.domain.model.Banco;
import backend.com.shared.domain.repository.BancoRepository;
import backend.com.shared.exception.BancoNotFoundException;
import backend.com.shared.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BancoServiceImpl implements BancoService {

    private final BancoRepository bancoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BancoResponse> listarTodos() {
        return bancoRepository.findAll().stream()
                .map(BancoResponse::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BancoResponse> obtenerPorId(Integer id) {
        return bancoRepository.findById(id).map(BancoResponse::fromDomain);
    }

    @Override
    public BancoResponse crear(BancoRequest request) {
        bancoRepository.findByCodigoBanco(request.getCodigoBanco().trim()).ifPresent(b -> {
            throw new BusinessRuleException(
                    "Ya existe un banco con el código: " + request.getCodigoBanco());
        });

        Banco banco = Banco.builder()
                .nombreBanco(request.getNombreBanco().trim())
                .codigoBanco(request.getCodigoBanco().trim().toUpperCase())
                .build();

        return BancoResponse.fromDomain(bancoRepository.save(banco));
    }

    @Override
    public BancoResponse actualizar(Integer id, BancoRequest request) {
        Banco existente = bancoRepository.findById(id)
                .orElseThrow(() -> new BancoNotFoundException(id));

        bancoRepository.findByCodigoBanco(request.getCodigoBanco().trim())
                .filter(b -> !b.getBancoId().equals(id))
                .ifPresent(b -> {
                    throw new BusinessRuleException(
                            "Ya existe un banco con el código: " + request.getCodigoBanco());
                });

        existente.setNombreBanco(request.getNombreBanco().trim());
        existente.setCodigoBanco(request.getCodigoBanco().trim().toUpperCase());

        return BancoResponse.fromDomain(bancoRepository.save(existente));
    }

    @Override
    public void eliminar(Integer id) {
        if (!bancoRepository.existsById(id)) {
            throw new BancoNotFoundException(id);
        }
        bancoRepository.deleteById(id);
    }
}
