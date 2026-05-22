package backend.com.shared.application.service.Impl;

import backend.com.shared.application.dto.PaisRequest;
import backend.com.shared.application.dto.PaisResponse;
import backend.com.shared.application.service.PaisService;
import backend.com.shared.domain.model.Pais;
import backend.com.shared.domain.repository.PaisRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.PaisNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaisServiceImpl implements PaisService {

    private final PaisRepository paisRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PaisResponse> listarTodos() {
        return paisRepository.findAll().stream()
                .map(PaisResponse::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaisResponse> obtenerPorId(Integer id) {
        return paisRepository.findById(id).map(PaisResponse::fromDomain);
    }

    @Override
    public PaisResponse crear(PaisRequest request) {
        paisRepository.findByNombrePais(request.getNombrePais()).ifPresent(p -> {
            throw new BusinessRuleException("Ya existe un país con el nombre: " + request.getNombrePais());
        });

        Pais pais = Pais.builder()
                .nombrePais(request.getNombrePais().trim())
                .build();

        return PaisResponse.fromDomain(paisRepository.save(pais));
    }

    @Override
    public PaisResponse actualizar(Integer id, PaisRequest request) {
        Pais existente = paisRepository.findById(id)
                .orElseThrow(() -> new PaisNotFoundException(id));

        paisRepository.findByNombrePais(request.getNombrePais())
                .filter(p -> !p.getIdPais().equals(id))
                .ifPresent(p -> {
                    throw new BusinessRuleException("Ya existe un país con el nombre: " + request.getNombrePais());
                });

        existente.setNombrePais(request.getNombrePais().trim());

        return PaisResponse.fromDomain(paisRepository.save(existente));
    }

    @Override
    public void eliminar(Integer id) {
        if (!paisRepository.existsById(id)) {
            throw new PaisNotFoundException(id);
        }
        paisRepository.deleteById(id);
    }
}
