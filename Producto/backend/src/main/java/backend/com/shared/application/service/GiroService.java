package backend.com.shared.application.service;

import backend.com.shared.domain.model.Giro;

import java.util.List;
import java.util.Optional;

public interface GiroService {

    List<Giro> listarTodos();

    Optional<Giro> obtenerPorId(Long giroId);

    Optional<Giro> obtenerPorCodigoActividad(String codigoActividad);

    List<Giro> buscarPorDescripcion(String descripcionGiro);

    List<Giro> obtenerPorTipoActividad(String tipoActividad);

    List<Giro> obtenerPorCategoriaTributaria(String categoriaTributaria);

    List<Giro> obtenerPorRegimenTributario(String regimenTributario);

    Giro crear(Giro giro);

    Giro actualizar(Long id, Giro giro);

    void eliminar(Long id);

    Optional<Giro> obtenerOCrearPorDescripcion(String descripcionGiro);
}
