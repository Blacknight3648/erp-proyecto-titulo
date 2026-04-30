package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.dto.ConfiguracionPlantillaDTO;
import backend.com.comercial.application.service.ConfiguracionPlantillaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/configuracion-plantillas")
@RequiredArgsConstructor
public class ConfiguracionPlantillaController {

    private final ConfiguracionPlantillaService service;

    @GetMapping
    public List<ConfiguracionPlantillaDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/by-nombre")
    public ConfiguracionPlantillaDTO findByNombre(@RequestParam String nombre) {
        return service.findByNombrePrenda(nombre);
    }

    @PostMapping
    public ConfiguracionPlantillaDTO save(@RequestBody ConfiguracionPlantillaDTO dto) {
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
