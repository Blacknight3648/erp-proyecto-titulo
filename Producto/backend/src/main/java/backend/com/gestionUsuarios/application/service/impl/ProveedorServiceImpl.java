package backend.com.gestionUsuarios.proveedor.application.service.impl;

import backend.com.gestionUsuarios.proveedor.application.service.ProveedorService;
import backend.com.gestionUsuarios.proveedor.application.service.ProveedorValidator;
import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;
import backend.com.gestionUsuarios.proveedor.infrastructure.exception.ProveedorNotFoundException;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.repository.ProveedorRepository;
import backend.com.shared.domain.model.Giro;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import backend.com.shared.infrastructure.persistence.repository.GiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    private final GiroRepository giroRepository;
    private final GiroMapper giroMapper;
    private final ProveedorValidator proveedorValidator;

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> obtenerPorId(Long proveedorId) {
        return proveedorRepository.findById(proveedorId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> obtenerPorRun(String runProveedor) {
        return proveedorRepository.findByRunProveedor(runProveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorRazonSocial(String razonSocial) {
        return proveedorRepository.buscarPorRazonSocial(razonSocial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerActivos() {
        return proveedorRepository.obtenerActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerInactivos() {
        return proveedorRepository.obtenerInactivos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerPorGiroId(Long giroId) {
        return proveedorRepository.obtenerPorGiroId(giroId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerPorSigla(String sigla) {
        return proveedorRepository.obtenerPorSigla(sigla);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerPorDescripcionGiro(String descripcionGiro) {
        return proveedorRepository.obtenerPorDescripcionGiro(descripcionGiro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerActivosPorSigla(String sigla) {
        return proveedorRepository.obtenerActivosPorSigla(sigla);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerActivosPorGiro(Long giroId) {
        return proveedorRepository.obtenerActivosPorGiro(giroId);
    }

    @Override
    public Proveedor crear(Proveedor proveedor) {
        proveedorValidator.validateUniqueness(proveedor.getRunProveedor());

        proveedor.setGiro(resolverGiro(proveedor.getGiro()));
        proveedor.setProveedorId(null);

        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor actualizar(Long proveedorId, Proveedor proveedor) {
        Proveedor existente = proveedorRepository.findById(proveedorId)
                .orElseThrow(() -> new ProveedorNotFoundException(proveedorId));

        if (proveedor.getRunProveedor() != null && !proveedor.getRunProveedor().equals(existente.getRunProveedor())) {
            proveedorValidator.validateUniqueness(proveedor.getRunProveedor());
            existente.setRunProveedor(proveedor.getRunProveedor());
        }
        if (proveedor.getRazonSocialProveedor() != null)
            existente.setRazonSocialProveedor(proveedor.getRazonSocialProveedor());
        if (proveedor.getTipoProveedor() != null)
            existente.setTipoProveedor(proveedor.getTipoProveedor());
        if (proveedor.getHorarioAtencion() != null)
            existente.setHorarioAtencion(proveedor.getHorarioAtencion());
        if (proveedor.getSigla() != null)
            existente.setSigla(proveedor.getSigla());
        existente.setActivo(proveedor.isActivo());

        Giro giro = resolverGiro(proveedor.getGiro());
        if (giro != null)
            existente.setGiro(giro);

        return proveedorRepository.save(existente);
    }

    @Override
    public void eliminar(Long proveedorId) {
        if (!proveedorRepository.existsById(proveedorId)) {
            throw new ProveedorNotFoundException(proveedorId);
        }
        proveedorRepository.deleteById(proveedorId);
    }

    private Giro resolverGiro(Giro giro) {
        if (giro == null || giro.getGiroId() == null)
            return null;
        Long giroId = giro.getGiroId();
        return giroRepository.findById(giroId)
                .map(giroMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Giro no encontrado con ID: " + giroId));
    }
}
