package backend.com.gestionUsuarios.application.service.impl;

import backend.com.gestionUsuarios.domain.model.User;
import backend.com.gestionUsuarios.domain.model.Vendedor;
import backend.com.gestionUsuarios.domain.repository.UserRepository;
import backend.com.gestionUsuarios.domain.repository.VendedorRepository;
import backend.com.gestionUsuarios.application.dto.VendedorCreateDTO;
import backend.com.gestionUsuarios.application.dto.VendedorDTO;
import backend.com.gestionUsuarios.application.service.VendedorService;
import backend.com.gestionUsuarios.infrastructure.exception.VendedorNotFoundException;
import backend.com.gestionUsuarios.infrastructure.mapper.VendedorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendedorServiceImpl implements VendedorService {

    private final VendedorRepository vendedorRepository;
    private final UserRepository userRepository;
    private final VendedorMapper vendedorMapper;

    @Override
    @Transactional
    public VendedorDTO create(VendedorCreateDTO dto) {
        if (vendedorRepository.existsByCodigoVendedor(dto.getCodigoVendedor())) {
            throw new RuntimeException("El código de vendedor ya existe: " + dto.getCodigoVendedor());
        }

        if (vendedorRepository.findByUsuario_UsuarioId(dto.getUsuarioId()).isPresent()) {
            throw new RuntimeException("El usuario ya está registrado como vendedor.");
        }

        User user = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getUsuarioId()));

        Vendedor vendedor = Vendedor.builder()
                .usuarioId(user.getUsuarioId())
                .codigoVendedor(dto.getCodigoVendedor())
                .activo(true)
                .nombreCompleto(user.getUsuarioNombre() + " " + user.getUsuarioApellidos())
                .build();

        Vendedor saved = vendedorRepository.save(vendedor);
        return vendedorMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public VendedorDTO update(Long id, VendedorCreateDTO dto) {
        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new VendedorNotFoundException("Vendedor no encontrado con id: " + id));

        if (!vendedor.getCodigoVendedor().equals(dto.getCodigoVendedor()) &&
                vendedorRepository.existsByCodigoVendedor(dto.getCodigoVendedor())) {
            throw new RuntimeException("El nuevo código de vendedor ya existe: " + dto.getCodigoVendedor());
        }

        vendedor.setCodigoVendedor(dto.getCodigoVendedor());

        if (!vendedor.getUsuarioId().equals(dto.getUsuarioId())) {
            User newUser = userRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getUsuarioId()));
            vendedor.setUsuarioId(newUser.getUsuarioId());
            vendedor.setNombreCompleto(newUser.getUsuarioNombre() + " " + newUser.getUsuarioApellidos());
        }

        Vendedor updated = vendedorRepository.save(vendedor);
        return vendedorMapper.toDTO(updated);
    }

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
        return vendedorRepository.findAll().stream()
                .map(vendedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new VendedorNotFoundException("Vendedor no encontrado con id: " + id));

        vendedor.setActivo(false);
        vendedorRepository.save(vendedor);
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
