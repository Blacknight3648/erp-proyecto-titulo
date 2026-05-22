package backend.com.shared.infrastructure.api;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import backend.com.shared.application.dto.TipoContactoDTO;
import backend.com.shared.application.service.TipoContactoService;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/api/v1/tipo-contacto")
@RequiredArgsConstructor
public class TipoContactoController {

    private final TipoContactoService tipoContactoService;

    @GetMapping
    public List<TipoContactoDTO> getAllTiposContacto() {
        return tipoContactoService.listarTodos();
    }

    @GetMapping("/{id}")
    public TipoContactoDTO getTipoContactoById(@PathVariable Long id) {
        return tipoContactoService.obtenerPorId(id).orElse(null);
    }

    @GetMapping("/nombre/{nombreTipoContacto}")
    public TipoContactoDTO getTipoContactoByNombreTipoContacto(@PathVariable String nombreTipoContacto) {
        return tipoContactoService.obtenerPorNombreTipoContacto(nombreTipoContacto).orElse(null);
    }

    @PostMapping
    public TipoContactoDTO createTipoContacto(@RequestBody TipoContactoDTO tipoContactoDTO) {
        return tipoContactoService.crearTipoContacto(tipoContactoDTO);
    }

    @PutMapping("/{id}")
    public TipoContactoDTO updateTipoContacto(@PathVariable Long id, @RequestBody TipoContactoDTO tipoContactoDTO) {
        return tipoContactoService.actualizarTipoContacto(id, tipoContactoDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTipoContacto(@PathVariable Long id) {
        tipoContactoService.eliminarTipoContacto(id);
    }

}
