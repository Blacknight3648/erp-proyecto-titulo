package backend.com.shared.application.service;

import backend.com.shared.application.dto.BancoDTO;
import backend.com.shared.domain.model.Banco;
import backend.com.shared.domain.repository.BancoRepository;
import backend.com.shared.exception.BancoNotFoundException;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.infrastructure.mapper.BancoMapper;
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
    private final BancoMapper bancoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BancoDTO> listarTodos() {
        return bancoRepository.findAll().stream()
                .map(bancoMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BancoDTO> obtenerPorId(Integer id) {
        return bancoRepository.findById(id).map(bancoMapper::toDTO);
    }

    @Override
    public BancoDTO crear(BancoDTO dto) {
        bancoRepository.findByCodigoBanco(dto.getCodigoBanco().trim()).ifPresent(b -> {
            throw new BusinessRuleException(
                    "Ya existe un banco con el código: " + dto.getCodigoBanco());
        });

        Banco banco = Banco.builder()
                .nombreBanco(dto.getNombreBanco().trim())
                .codigoBanco(dto.getCodigoBanco().trim().toUpperCase())
                .build();

        return bancoMapper.toDTO(bancoRepository.save(banco));
    }

    @Override
    public BancoDTO actualizar(Integer id, BancoDTO dto) {
        Banco existente = bancoRepository.findById(id)
                .orElseThrow(() -> new BancoNotFoundException(id));

        bancoRepository.findByCodigoBanco(dto.getCodigoBanco().trim())
                .filter(b -> !b.getBancoId().equals(id))
                .ifPresent(b -> {
                    throw new BusinessRuleException(
                            "Ya existe un banco con el código: " + dto.getCodigoBanco());
                });

        existente.setNombreBanco(dto.getNombreBanco().trim());
        existente.setCodigoBanco(dto.getCodigoBanco().trim().toUpperCase());

        return bancoMapper.toDTO(bancoRepository.save(existente));
    }

    @Override
    public void eliminar(Integer id) {
        if (!bancoRepository.existsById(id)) {
            throw new BancoNotFoundException(id);
        }
        bancoRepository.deleteById(id);
    }
}