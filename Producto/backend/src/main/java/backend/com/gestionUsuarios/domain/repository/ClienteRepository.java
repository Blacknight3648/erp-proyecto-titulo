package backend.com.gestionUsuarios.domain.repository;

import backend.com.gestionUsuarios.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    List<Cliente> findAll();

    Optional<Cliente> findByRunCliente(String runCliente);

    boolean existsByRunCliente(String runCliente);

    List<Cliente> buscarPorRazonSocial(String razonSocial);

    List<Cliente> obtenerPorSigla(String sigla);

    List<Cliente> obtenerPorGiroId(Long giroId);

    List<Cliente> obtenerPorDescripcionGiro(String descripcionGiro);

    List<Cliente> findByActivoTrue();

    List<Cliente> findByActivoFalse();

    List<Cliente> obtenerActivosPorSigla(String sigla);

    List<Cliente> obtenerActivosPorGiro(Long giroId);

    boolean existsById(Long id);

    void deleteById(Long id);
}
