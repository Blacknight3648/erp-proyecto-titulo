package backend.com.gestionUsuarios.application.service;

import backend.com.gestionUsuarios.domain.model.Area;
import java.util.List;

public interface AreaService {
    Area obtenerArea(Long id);
    List<Area> listarAreas();
    Area crearArea(Area area);
    Area actualizarArea(Long id, Area area);
    void eliminarArea(Long id);
}
