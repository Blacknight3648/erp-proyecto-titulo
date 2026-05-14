package backend.com.gestionUsuarios.cliente.application.service;

import backend.com.gestionUsuarios.cliente.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    List<Cliente> listarTodos();

    Optional<Cliente> obtenerPorId(Long clienteId);

    Optional<Cliente> obtenerPorRun(String runCliente);

    List<Cliente> buscarPorRazonSocial(String razonSocial);

    List<Cliente> obtenerActivos();

    List<Cliente> obtenerInactivos();

    List<Cliente> obtenerPorSiglaId(Long siglaId);

    List<Cliente> obtenerPorGiroId(Long giroId);

    List<Cliente> obtenerPorDescripcionSigla(String descripcionSigla);

    List<Cliente> obtenerPorDescripcionGiro(String descripcionGiro);

    List<Cliente> obtenerActivosPorSigla(Long siglaId);

    List<Cliente> obtenerActivosPorGiro(Long giroId);

    Cliente crear(Cliente cliente);

    Cliente actualizar(Long clienteId, Cliente cliente);

    void eliminar(Long clienteId);
}
