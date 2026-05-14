package backend.com.gestionUsuarios.proveedor.application.service;

import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;
import backend.com.gestionUsuarios.proveedor.infrastructure.exception.ProveedorNotFoundException;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.repository.ProveedorRepository;
import backend.com.shared.domain.model.Giro;
import backend.com.shared.domain.model.Sigla;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import backend.com.shared.infrastructure.mapper.SiglaMapper;
import backend.com.shared.infrastructure.persistence.repository.GiroRepository;
import backend.com.shared.infrastructure.persistence.repository.SiglaRepository;
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
    private final SiglaRepository siglaRepository;
    private final GiroRepository giroRepository;
    private final SiglaMapper siglaMapper;
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
    public List<Proveedor> obtenerPorSiglaId(Long siglaId) {
        return proveedorRepository.obtenerPorSiglaId(siglaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerPorGiroId(Long giroId) {
        return proveedorRepository.obtenerPorGiroId(giroId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerPorSiglaAbreviatura(String siglaAbreviatura) {
        return proveedorRepository.obtenerPorSiglaAbreviatura(siglaAbreviatura);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerPorDescripcionGiro(String descripcionGiro) {
        return proveedorRepository.obtenerPorDescripcionGiro(descripcionGiro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerActivosPorSigla(Long siglaId) {
        return proveedorRepository.obtenerActivosPorSigla(siglaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerActivosPorGiro(Long giroId) {
        return proveedorRepository.obtenerActivosPorGiro(giroId);
    }

    @Override
    public Proveedor crear(Proveedor proveedor) {
        proveedorValidator.validateUniqueness(proveedor.getRunProveedor());

        proveedor.setSigla(resolverSigla(proveedor.getSigla()));
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
        if (proveedor.getRazonSocialProveedor() != null) existente.setRazonSocialProveedor(proveedor.getRazonSocialProveedor());
        if (proveedor.getDireccionProveedor() != null) existente.setDireccionProveedor(proveedor.getDireccionProveedor());
        if (proveedor.getContactoProveedor() != null) existente.setContactoProveedor(proveedor.getContactoProveedor());
        if (proveedor.getEmailProveedor() != null) existente.setEmailProveedor(proveedor.getEmailProveedor());
        if (proveedor.getTelefonoProveedor() != null) existente.setTelefonoProveedor(proveedor.getTelefonoProveedor());
        if (proveedor.getTipoProveedor() != null) existente.setTipoProveedor(proveedor.getTipoProveedor());
        existente.setActivo(proveedor.isActivo());

        Sigla sigla = resolverSigla(proveedor.getSigla());
        if (sigla != null) existente.setSigla(sigla);

        Giro giro = resolverGiro(proveedor.getGiro());
        if (giro != null) existente.setGiro(giro);

        return proveedorRepository.save(existente);
    }

    @Override
    public void eliminar(Long proveedorId) {
        if (!proveedorRepository.existsById(proveedorId)) {
            throw new ProveedorNotFoundException(proveedorId);
        }
        proveedorRepository.deleteById(proveedorId);
    }

    private Sigla resolverSigla(Sigla sigla) {
        if (sigla == null || sigla.getSiglaId() == null) return null;
        Long siglaId = sigla.getSiglaId();
        return siglaRepository.findById(siglaId)
                .map(siglaMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Sigla no encontrada con ID: " + siglaId));
    }

    private Giro resolverGiro(Giro giro) {
        if (giro == null || giro.getGiroId() == null) return null;
        Long giroId = giro.getGiroId();
        return giroRepository.findById(giroId)
                .map(giroMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Giro no encontrado con ID: " + giroId));
    }
}
