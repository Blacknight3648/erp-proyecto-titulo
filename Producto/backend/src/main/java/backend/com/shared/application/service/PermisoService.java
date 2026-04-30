package backend.com.shared.application.service;

import backend.com.shared.domain.model.Permiso;
import backend.com.shared.application.dto.PermisoDTO;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface PermisoService {

    Permiso crearPermiso(Permiso permiso);

    List<Permiso> listarPermisos();

    List<Permiso> listarPermisosPorModulo(String modulo);

    Optional<Permiso> obtenerPermisoPorId(@NonNull Long id);

    Optional<Permiso> obtenerPermisoPorNombre(String nombre);

    Permiso actualizarPermiso(@NonNull Long id, Permiso permisoActualizado);

    void eliminarPermiso(@NonNull Long id);

    List<PermisoDTO> obtenerPermisosAgrupadosPorModulo();
}
