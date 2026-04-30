package backend.com.gestionUsuarios.vendedor.application.service;

import backend.com.gestionUsuarios.usuario.domain.model.User;
import backend.com.gestionUsuarios.usuario.infrastructure.persistence.repository.UserRepository;
import backend.com.gestionUsuarios.vendedor.application.dto.VendedorCreateDTO;
import backend.com.gestionUsuarios.vendedor.application.dto.VendedorDTO;
import backend.com.gestionUsuarios.vendedor.infrastructure.exception.VendedorNotFoundException;
import backend.com.gestionUsuarios.vendedor.infrastructure.mapper.VendedorMapper;
import backend.com.gestionUsuarios.vendedor.infrastructure.persistence.repository.VendedorRepository;
import backend.com.gestionUsuarios.vendedor.infrastructure.persistence.entity.VendedorJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendedorServiceImpl implements VendedorService {

    private final VendedorRepository vendedorRepository;
    private final UserRepository userRepository;
    private final VendedorMapper vendedorMapper;

    @Override
    @Transactional
    public VendedorDTO create(VendedorCreateDTO dto) {
        // Validar unicidad de código
        if (vendedorRepository.existsByCodigoVendedor(dto.getCodigoVendedor())) {
            throw new RuntimeException("El código de vendedor ya existe: " + dto.getCodigoVendedor());
        }

        // Validar si el usuario ya es vendedor
        if (vendedorRepository.findByUsuario_UsuarioId(dto.getUsuarioId()).isPresent()) {
            throw new RuntimeException("El usuario ya está registrado como vendedor.");
        }

        // Buscar usuario
        @SuppressWarnings("null")
        User user = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getUsuarioId()));

        VendedorJpaEntity entity = new VendedorJpaEntity();
        entity.setUsuario(user);
        entity.setCodigoVendedor(dto.getCodigoVendedor());

        entity.setActivo(true); // Default active

        VendedorJpaEntity saved = vendedorRepository.save(entity);
        return vendedorMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public VendedorDTO update(Long id, VendedorCreateDTO dto) {
        @SuppressWarnings("null")
        VendedorJpaEntity entity = vendedorRepository.findById(id)
                .orElseThrow(() -> new VendedorNotFoundException("Vendedor no encontrado con id: " + id));

        // Si cambia el código, validar unicidad
        if (!entity.getCodigoVendedor().equals(dto.getCodigoVendedor()) &&
                vendedorRepository.existsByCodigoVendedor(dto.getCodigoVendedor())) {
            throw new RuntimeException("El nuevo código de vendedor ya existe: " + dto.getCodigoVendedor());
        }

        entity.setCodigoVendedor(dto.getCodigoVendedor());

        // El usuario generalmente no se cambia, pero si fuera necesario se validaría
        if (!entity.getUsuario().getUsuarioId().equals(dto.getUsuarioId())) {
            @SuppressWarnings("null")
            User newUser = userRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getUsuarioId()));
            entity.setUsuario(newUser);
        }

        VendedorJpaEntity updated = vendedorRepository.save(entity);
        return vendedorMapper.toDTO(updated);
    }

    @SuppressWarnings("null")
    @Override
    @Transactional(readOnly = true)
    public VendedorDTO findById(Long id) {
        return vendedorRepository.findById(id)
                .map(vendedorMapper::toDTO)
                .orElseThrow(() -> new VendedorNotFoundException("Vendedor no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendedorDTO> findAll() {
        return vendedorMapper.toDTOList(vendedorRepository.findAll());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        @SuppressWarnings("null")
        VendedorJpaEntity entity = vendedorRepository.findById(id)
                .orElseThrow(() -> new VendedorNotFoundException("Vendedor no encontrado con id: " + id));

        // Soft delete consistent with project style if aplicable, or hard delete
        // Given BaseEntity has 'activo', we use soft delete.
        entity.setActivo(false);
        vendedorRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public VendedorDTO findByUsuarioId(Long usuarioId) {
        return vendedorRepository.findByUsuario_UsuarioId(usuarioId)
                .map(vendedorMapper::toDTO)
                .orElseThrow(
                        () -> new VendedorNotFoundException("Vendedor no encontrado para el usuario id: " + usuarioId));
    }
}
