package backend.com.shared.application.service;

import backend.com.shared.application.dto.DireccionRequest;
import backend.com.shared.application.dto.DireccionResponse;
import backend.com.shared.domain.model.Comuna;
import backend.com.shared.domain.model.Direccion;
import backend.com.shared.domain.model.TipoDireccion;
import backend.com.shared.domain.repository.ComunaRepository;
import backend.com.shared.domain.repository.DireccionRepository;
import backend.com.shared.domain.repository.TipoDireccionRepository;
import backend.com.shared.exception.ComunaNotFoundException;
import backend.com.shared.exception.DireccionNotFoundException;
import backend.com.shared.exception.TipoDireccionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository direccionRepository;
    private final ComunaRepository comunaRepository;
    private final TipoDireccionRepository tipoDireccionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DireccionResponse> listarTodos() {
        return direccionRepository.findAll().stream()
                .map(DireccionResponse::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DireccionResponse> obtenerPorId(Long id) {
        return direccionRepository.findById(id).map(DireccionResponse::fromDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DireccionResponse> listarPorComuna(Long comunaId) {
        if (!comunaRepository.existsById(comunaId)) {
            throw new ComunaNotFoundException(comunaId);
        }
        return direccionRepository.findByComunaId(comunaId).stream()
                .map(DireccionResponse::fromDomain)
                .toList();
    }

    @Override
    public DireccionResponse crear(DireccionRequest request) {
        TipoDireccion tipoDireccion = tipoDireccionRepository.findById(request.getTipoDireccionId())
                .orElseThrow(() -> new TipoDireccionNotFoundException(request.getTipoDireccionId()));

        Comuna comuna = comunaRepository.findById(request.getComunaId())
                .orElseThrow(() -> new ComunaNotFoundException(request.getComunaId()));

        Direccion direccion = Direccion.builder()
                .calle(request.getCalle().trim())
                .numero(request.getNumero().trim())
                .depto(request.getDepto() != null ? request.getDepto().trim() : null)
                .tipoDireccion(tipoDireccion)
                .comuna(comuna)
                .build();

        return DireccionResponse.fromDomain(direccionRepository.save(direccion));
    }

    @Override
    public DireccionResponse actualizar(Long id, DireccionRequest request) {
        Direccion existente = direccionRepository.findById(id)
                .orElseThrow(() -> new DireccionNotFoundException(id));

        TipoDireccion tipoDireccion = tipoDireccionRepository.findById(request.getTipoDireccionId())
                .orElseThrow(() -> new TipoDireccionNotFoundException(request.getTipoDireccionId()));

        Comuna comuna = comunaRepository.findById(request.getComunaId())
                .orElseThrow(() -> new ComunaNotFoundException(request.getComunaId()));

        existente.setCalle(request.getCalle().trim());
        existente.setNumero(request.getNumero().trim());
        existente.setDepto(request.getDepto() != null ? request.getDepto().trim() : null);
        existente.setTipoDireccion(tipoDireccion);
        existente.setComuna(comuna);

        return DireccionResponse.fromDomain(direccionRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new DireccionNotFoundException(id);
        }
        direccionRepository.deleteById(id);
    }
}
