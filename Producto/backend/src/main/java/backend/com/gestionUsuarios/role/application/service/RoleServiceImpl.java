package backend.com.gestionUsuarios.role.application.service;

import backend.com.gestionUsuarios.area.domain.model.Area;
import backend.com.gestionUsuarios.area.domain.repository.AreaRepository;
import java.util.Objects;
import backend.com.gestionUsuarios.role.domain.model.Role;
import backend.com.gestionUsuarios.role.infrastructure.exception.RoleDuplicadoException;
import backend.com.gestionUsuarios.role.infrastructure.exception.RoleNotFoundException;
import backend.com.gestionUsuarios.role.infrastructure.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final AreaRepository areaRepository;

    @Override
    @Transactional(readOnly = true)
    public Role obtenerRole(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("El ID del rol no puede ser nulo");
        }
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> listarRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public Role crearRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }

        if (roleRepository.existsByNombre(role.getNombre())) {
            throw new RoleDuplicadoException(role.getNombre());
        }

        if (role.getArea() == null || role.getArea().getAreaId() == null) {
            throw new RuntimeException("El área es obligatoria");
        }

        Long areaId = Objects.requireNonNull(role.getArea().getAreaId());
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        role.setArea(area);

        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role actualizarRole(Long id, Role roleActualizado) {

        Role role = obtenerRole(id);

        if (!role.getNombre().equals(roleActualizado.getNombre()) &&
                roleRepository.existsByNombre(roleActualizado.getNombre())) {
            throw new RoleDuplicadoException(roleActualizado.getNombre());
        }

        role.setNombre(roleActualizado.getNombre());
        role.setDescripcion(roleActualizado.getDescripcion());

        if (roleActualizado != null && roleActualizado.getArea() != null
                && roleActualizado.getArea().getAreaId() != null) {
            Long areaId = Objects.requireNonNull(roleActualizado.getArea().getAreaId());
            Area area = areaRepository.findById(areaId)
                    .orElseThrow(() -> new RuntimeException("Área no encontrada"));

            role.setArea(area);
        }

        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void eliminarRole(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }

        if (!roleRepository.existsById(id)) {
            throw new RoleNotFoundException(id);
        }

        roleRepository.deleteById(id);
    }
}