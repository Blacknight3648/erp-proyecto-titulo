package backend.com.gestionUsuarios.cliente.application.service;

import backend.com.gestionUsuarios.cliente.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    Cliente crearCliente(Cliente cliente);

    Cliente actualizarCliente(Long id, Cliente cliente);

    Optional<Cliente> obtenerClientePorId(Long id);

    Optional<Cliente> obtenerClientePorRun(String runCliente);

    List<Cliente> listarClientes();

    void eliminarCliente(Long id);

}
