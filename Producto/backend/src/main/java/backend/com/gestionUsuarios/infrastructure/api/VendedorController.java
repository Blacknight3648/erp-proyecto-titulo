package backend.com.gestionUsuarios.vendedor.infrastructure.api;

import backend.com.gestionUsuarios.vendedor.application.dto.VendedorCreateDTO;
import backend.com.gestionUsuarios.vendedor.application.dto.VendedorDTO;
import backend.com.gestionUsuarios.vendedor.application.service.VendedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendedores")
@RequiredArgsConstructor
public class VendedorController {

    private final VendedorService vendedorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendedorDTO create(@Valid @RequestBody VendedorCreateDTO dto) {
        return vendedorService.create(dto);
    }

    @PutMapping("/{id}")
    public VendedorDTO update(@PathVariable Long id, @Valid @RequestBody VendedorCreateDTO dto) {
        return vendedorService.update(id, dto);
    }

    @GetMapping("/{id}")
    public VendedorDTO findById(@PathVariable Long id) {
        return vendedorService.findById(id);
    }

    @GetMapping
    public List<VendedorDTO> findAll() {
        return vendedorService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vendedorService.delete(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public VendedorDTO findByUsuarioId(@PathVariable Long usuarioId) {
        return vendedorService.findByUsuarioId(usuarioId);
    }
}
