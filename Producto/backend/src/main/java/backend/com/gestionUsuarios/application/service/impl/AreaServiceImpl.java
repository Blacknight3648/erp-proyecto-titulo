package backend.com.gestionUsuarios.application.service.impl;

import backend.com.gestionUsuarios.application.service.AreaService;
import backend.com.gestionUsuarios.domain.model.Area;
import backend.com.gestionUsuarios.infrastructure.exception.AreaDuplicadaException;
import backend.com.gestionUsuarios.infrastructure.exception.AreaNotFoundException;
import backend.com.gestionUsuarios.domain.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepository;

    @Override
    @Transactional(readOnly = true)
    public Area obtenerArea(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del área no puede ser nulo");
        }
        return areaRepository.findById(id)
                .orElseThrow(() -> new AreaNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Area> listarAreas() {
        return areaRepository.findAll();
    }

    @Override
    @Transactional
    public Area crearArea(Area area) {
        if (area == null) {
            throw new IllegalArgumentException("El área no puede ser nula");
        }
        if (areaRepository.existsByNombre(area.getNombre())) {
            throw new AreaDuplicadaException(area.getNombre());
        }
        return areaRepository.save(area);
    }

    @Override
    @Transactional
    public Area actualizarArea(Long id, Area areaActualizada) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del área no puede ser nulo");
        }
        if (areaActualizada == null) {
            throw new IllegalArgumentException("El área actualizada no puede ser nula");
        }

        Area area = obtenerArea(id);

        if (!area.getNombre().equals(areaActualizada.getNombre()) &&
                areaRepository.existsByNombre(areaActualizada.getNombre())) {
            throw new AreaDuplicadaException(areaActualizada.getNombre());
        }

        area.setNombre(areaActualizada.getNombre());
        area.setDescripcion(areaActualizada.getDescripcion());

        return areaRepository.save(area);
    }

    @Override
    @Transactional
    public void eliminarArea(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del área no puede ser nulo");
        }
        if (!areaRepository.existsById(id)) {
            throw new AreaNotFoundException(id);
        }
        areaRepository.deleteById(id);
    }
}
