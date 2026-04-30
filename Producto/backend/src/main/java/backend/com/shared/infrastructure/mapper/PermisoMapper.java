package backend.com.shared.infrastructure.mapper;

import backend.com.shared.domain.model.Permiso;
import backend.com.shared.application.dto.PermisoDTO;

public class PermisoMapper {

    public static PermisoDTO toPermisoDTO(Permiso permiso) {
        if (permiso == null) {
            return null;
        }

        return PermisoDTO.builder()
                .id(permiso.getId())
                .nombre(permiso.getNombre())
                .descripcion(permiso.getDescripcion())
                .modulo(permiso.getModulo())
                .build();
    }

    public static Permiso toPermiso(PermisoDTO dto) {
        if (dto == null) {
            return null;
        }

        return Permiso.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .modulo(dto.getModulo())
                .build();
    }
}
