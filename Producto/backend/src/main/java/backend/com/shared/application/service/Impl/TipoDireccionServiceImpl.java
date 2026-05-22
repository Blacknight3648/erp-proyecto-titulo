package backend.com.shared.application.service.Impl;

import backend.com.shared.application.dto.TipoDireccionRequest;
import backend.com.shared.application.dto.TipoDireccionResponse;
import backend.com.shared.application.service.TipoDireccionService;
import backend.com.shared.domain.model.TipoDireccion;
import backend.com.shared.domain.repository.TipoDireccionRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.TipoDireccionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoDireccionServiceImpl implements TipoDireccionService {

    private final TipoDireccionRepository tipoDireccionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoDireccionResponse> listarTodos() {
        return tipoDireccionRepository.findAll().stream()
                .map(TipoDireccionResponse::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoDireccionResponse> obtenerPorId(Integer id) {
        return tipoDireccionRepository.findById(id).map(TipoDireccionResponse::fromDomain);
    }

    @Override
    public TipoDireccionResponse crear(TipoDireccionRequest request) {
        boolean descripcionDuplicada = tipoDireccionRepository.findAll().stream()
                .anyMatch(t -> t.getDescripcion().equalsIgnoreCase(request.getDescripcion().trim()));

        if (descripcionDuplicada) {
            throw new BusinessRuleException(
                    "Ya existe un tipo de dirección con la descripción: " + request.getDescripcion());
        }

        TipoDireccion tipoDireccion = TipoDireccion.builder()
                .descripcion(request.getDescripcion().trim())
                .build();

        return TipoDireccionResponse.fromDomain(tipoDireccionRepository.save(tipoDireccion));
    }

    @Override
    public TipoDireccionResponse actualizar(Integer id, TipoDireccionRequest request) {
        TipoDireccion existente = tipoDireccionRepository.findById(id)
                .orElseThrow(() -> new TipoDireccionNotFoundException(id));

        boolean descripcionDuplicada = tipoDireccionRepository.findAll().stream()
                .filter(t -> !t.getTipoDireccionId().equals(id))
                .anyMatch(t -> t.getDescripcion().equalsIgnoreCase(request.getDescripcion().trim()));

        if (descripcionDuplicada) {
            throw new BusinessRuleException(
                    "Ya existe un tipo de dirección con la descripción: " + request.getDescripcion());
        }

        existente.setDescripcion(request.getDescripcion().trim());

        return TipoDireccionResponse.fromDomain(tipoDireccionRepository.save(existente));
    }

    @Override
    public void eliminar(Integer id) {
        if (!tipoDireccionRepository.existsById(id)) {
            throw new TipoDireccionNotFoundException(id);
        }
        tipoDireccionRepository.deleteById(id);
    }
}
