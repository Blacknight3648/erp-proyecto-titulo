package backend.com.shared.application.service;

import backend.com.shared.domain.model.Giro;

import java.util.List;
import java.util.Optional;

public interface GiroService {

    List<Giro> listarTodos();

    Optional<Giro> obtenerPorId(Long giroId);

    Optional<Giro> obtenerPorCodigoSii(String codigoSii);

    List<Giro> buscarPorNombreGiro(String nombreGiro);

    List<Giro> obtenerPorDescripcionGiro(String descripcionGiro);

    Giro crear(Giro giro);

    Giro actualizar(Long id, Giro giro);

    void eliminar(Long id);

    Optional<Giro> obtenerOCrearPorNombreGiro(String nombreGiro);
}
