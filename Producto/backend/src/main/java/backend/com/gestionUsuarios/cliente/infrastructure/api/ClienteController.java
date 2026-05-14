package backend.com.gestionUsuarios.cliente.infrastructure.api;

import backend.com.gestionUsuarios.cliente.application.dto.ClienteDTO;
import backend.com.gestionUsuarios.cliente.application.service.ClienteService;
import backend.com.gestionUsuarios.cliente.domain.model.Cliente;
import backend.com.gestionUsuarios.cliente.infrastructure.mapper.ClienteMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        return ResponseEntity.ok(clienteMapper.toDTOList(clienteService.listarTodos()));
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<ClienteDTO> obtenerPorId(@PathVariable Long clienteId) {
        return clienteService.obtenerPorId(clienteId)
                .map(clienteMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/run/{runCliente}")
    public ResponseEntity<ClienteDTO> obtenerPorRun(@PathVariable String runCliente) {
        return clienteService.obtenerPorRun(runCliente)
                .map(clienteMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/razon-social/{razonSocial}")
    public ResponseEntity<List<ClienteDTO>> buscarPorRazonSocial(@PathVariable String razonSocial) {
        return ResponseEntity.ok(clienteMapper.toDTOList(clienteService.buscarPorRazonSocial(razonSocial)));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ClienteDTO>> obtenerActivos() {
        return ResponseEntity.ok(clienteMapper.toDTOList(clienteService.obtenerActivos()));
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<ClienteDTO>> obtenerInactivos() {
        return ResponseEntity.ok(clienteMapper.toDTOList(clienteService.obtenerInactivos()));
    }

    @GetMapping("/sigla/{siglaId}")
    public ResponseEntity<List<ClienteDTO>> obtenerPorSiglaId(@PathVariable Long siglaId) {
        return ResponseEntity.ok(clienteMapper.toDTOList(clienteService.obtenerPorSiglaId(siglaId)));
    }

    @GetMapping("/giro/{giroId}")
    public ResponseEntity<List<ClienteDTO>> obtenerPorGiroId(@PathVariable Long giroId) {
        return ResponseEntity.ok(clienteMapper.toDTOList(clienteService.obtenerPorGiroId(giroId)));
    }

    @GetMapping("/sigla/descripcion/{descripcionSigla}")
    public ResponseEntity<List<ClienteDTO>> obtenerPorDescripcionSigla(@PathVariable String descripcionSigla) {
        return ResponseEntity.ok(clienteMapper.toDTOList(clienteService.obtenerPorDescripcionSigla(descripcionSigla)));
    }

    @GetMapping("/giro/descripcion/{descripcionGiro}")
    public ResponseEntity<List<ClienteDTO>> obtenerPorDescripcionGiro(@PathVariable String descripcionGiro) {
        return ResponseEntity.ok(clienteMapper.toDTOList(clienteService.obtenerPorDescripcionGiro(descripcionGiro)));
    }

    @GetMapping("/activos/sigla/{siglaId}")
    public ResponseEntity<List<ClienteDTO>> obtenerActivosPorSigla(@PathVariable Long siglaId) {
        return ResponseEntity.ok(clienteMapper.toDTOList(clienteService.obtenerActivosPorSigla(siglaId)));
    }

    @GetMapping("/activos/giro/{giroId}")
    public ResponseEntity<List<ClienteDTO>> obtenerActivosPorGiro(@PathVariable Long giroId) {
        return ResponseEntity.ok(clienteMapper.toDTOList(clienteService.obtenerActivosPorGiro(giroId)));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crear(@Valid @RequestBody ClienteDTO dto) {
        Cliente creado = clienteService.crear(clienteMapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toDTO(creado));
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<ClienteDTO> actualizar(@PathVariable Long clienteId, @Valid @RequestBody ClienteDTO dto) {
        Cliente actualizado = clienteService.actualizar(clienteId, clienteMapper.toDomain(dto));
        return ResponseEntity.ok(clienteMapper.toDTO(actualizado));
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long clienteId) {
        clienteService.eliminar(clienteId);
        return ResponseEntity.noContent().build();
    }
}
