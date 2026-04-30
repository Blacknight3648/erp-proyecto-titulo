package backend.com.shared.application.service;

import backend.com.shared.infrastructure.mapper.PermisoMapper;
import backend.com.shared.domain.model.Permiso;
import backend.com.shared.application.dto.PermisoDTO;
import backend.com.shared.infrastructure.persistence.repository.PermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermisoServiceImpl implements PermisoService {

    private final PermisoRepository permisoRepository;

    @Override
    public Permiso crearPermiso(Permiso permiso) {
        if (permisoRepository.existsByNombre(permiso.getNombre())) {
            throw new IllegalArgumentException("Ya existe un permiso con el nombre: " + permiso.getNombre());
        }
        return permisoRepository.save(permiso);
    }

    @Override
    public List<Permiso> listarPermisos() {
        return permisoRepository.findAll();
    }

    @Override
    public List<Permiso> listarPermisosPorModulo(String modulo) {
        return permisoRepository.findByModulo(modulo);
    }

    @Override
    public Optional<Permiso> obtenerPermisoPorId(@NonNull Long id) {
        return permisoRepository.findById(id);
    }

    @Override
    public Optional<Permiso> obtenerPermisoPorNombre(String nombre) {
        return permisoRepository.findByNombre(nombre);
    }

    @Override
    public Permiso actualizarPermiso(@NonNull Long id, Permiso permisoActualizado) {
        return permisoRepository.findById(id).map(permiso -> {
            permiso.setNombre(permisoActualizado.getNombre());
            permiso.setDescripcion(permisoActualizado.getDescripcion());
            permiso.setModulo(permisoActualizado.getModulo());
            return permisoRepository.save(permiso);
        }).orElseThrow(() -> new IllegalArgumentException("No se encontró el permiso con ID: " + id));
    }

    @Override
    public void eliminarPermiso(@NonNull Long id) {
        if (!permisoRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró el permiso con ID: " + id);
        }
        permisoRepository.deleteById(id);
    }

    @Override
    public List<PermisoDTO> obtenerPermisosAgrupadosPorModulo() {
        return permisoRepository.findAll().stream()
                .map(PermisoMapper::toPermisoDTO)
                .collect(Collectors.toList());
    }
}
