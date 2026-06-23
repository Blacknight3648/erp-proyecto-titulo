package backend.com.gestionUsuarios.application.service;

import backend.com.gestionUsuarios.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    List<Cliente> listarTodos();

    Optional<Cliente> obtenerPorId(Long clienteId);

    Optional<Cliente> obtenerPorRun(String runCliente);

    List<Cliente> buscarPorRazonSocial(String razonSocial);

    List<Cliente> obtenerActivos();

    List<Cliente> obtenerInactivos();

    List<Cliente> obtenerPorSigla(String sigla);

    List<Cliente> obtenerPorGiroId(Long giroId);

    List<Cliente> obtenerPorDescripcionGiro(String descripcionGiro);

    List<Cliente> obtenerActivosPorSigla(String sigla);

    List<Cliente> obtenerActivosPorGiro(Long giroId);

    Cliente crear(Cliente cliente);

    Cliente actualizar(Long clienteId, Cliente cliente);

    void eliminar(Long clienteId);
}
