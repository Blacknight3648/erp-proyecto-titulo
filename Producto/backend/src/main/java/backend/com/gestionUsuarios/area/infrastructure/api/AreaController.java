package backend.com.gestionUsuarios.area.infrastructure.api;

import backend.com.gestionUsuarios.area.domain.model.Area;
import backend.com.gestionUsuarios.area.application.dto.AreaDTO;
import backend.com.gestionUsuarios.area.infrastructure.mapper.AreaMapper;
import backend.com.gestionUsuarios.area.application.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/areas")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;
    private final AreaMapper areaMapper;

    @GetMapping
    public ResponseEntity<List<AreaDTO>> listarAreas() {
        return ResponseEntity.ok(areaMapper.toDTOList(areaService.listarAreas()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(areaMapper.toDTO(areaService.obtenerArea(id)));
    }

    @PostMapping
    public ResponseEntity<AreaDTO> crear(@RequestBody AreaDTO areaDTO) {
        Area area = areaMapper.toEntity(areaDTO);
        Area nueva = areaService.crearArea(area);
        return ResponseEntity.status(HttpStatus.CREATED).body(areaMapper.toDTO(nueva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaDTO> actualizar(@PathVariable Long id, @RequestBody AreaDTO areaDTO) {
        Area area = areaMapper.toEntity(areaDTO);
        Area actualizada = areaService.actualizarArea(id, area);
        return ResponseEntity.ok(areaMapper.toDTO(actualizada));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AreaDTO> actualizarParcial(@PathVariable Long id, @RequestBody AreaDTO areaDTO) {
        Area areaExistente = areaService.obtenerArea(id);

        if (areaDTO.getNombre() != null) {
            areaExistente.setNombre(areaDTO.getNombre());
        }
        if (areaDTO.getDescripcion() != null) {
            areaExistente.setDescripcion(areaDTO.getDescripcion());
        }

        Area actualizada = areaService.actualizarArea(id, areaExistente);
        return ResponseEntity.ok(areaMapper.toDTO(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        areaService.eliminarArea(id);
        return ResponseEntity.noContent().build();
    }
}