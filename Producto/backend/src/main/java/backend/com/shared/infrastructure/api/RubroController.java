package backend.com.shared.infrastructure.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.com.shared.application.service.RubroService;
import backend.com.shared.application.dto.RubroDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/rubros")
@RequiredArgsConstructor
public class RubroController {

    private final RubroService rubroService;

    @GetMapping
    public List<RubroDTO> getAllRubros() {
        return rubroService.getAllRubros();
    }

    @GetMapping("/{id}")
    public RubroDTO getRubroById(@PathVariable Long id) {
        return rubroService.getRubroById(id).orElse(null);
    }

    @GetMapping("/codigo/{codigoSii}")
    public RubroDTO getRubroByCodigoSii(@PathVariable String codigoSii) {
        return rubroService.getRubroByCodigoSii(codigoSii).orElse(null);
    }

    @GetMapping("/nombre/{nombreRubro}")
    public RubroDTO getRubroByNombreRubro(@PathVariable String nombreRubro) {
        return rubroService.getRubroByNombreRubro(nombreRubro).orElse(null);
    }

    @PostMapping
    public RubroDTO createRubro(@RequestBody RubroDTO rubro) {
        return rubroService.createRubro(rubro);
    }

    @PutMapping("/{id}")
    public RubroDTO updateRubro(@PathVariable Long id, @RequestBody RubroDTO rubro) {
        return rubroService.updateRubro(id, rubro).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteRubro(@PathVariable Long id) {
        rubroService.deleteRubro(id);
    }

}
