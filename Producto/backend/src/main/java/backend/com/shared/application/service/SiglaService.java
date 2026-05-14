package backend.com.shared.application.service;

import backend.com.shared.domain.model.Sigla;

import java.util.List;
import java.util.Optional;

public interface SiglaService {

    List<Sigla> obtenerTodos();

    Optional<Sigla> obtenerPorId(Long siglaId);

    List<Sigla> obtenerPorDescripcionSigla(String descripcionSigla);

    Optional<Sigla> obtenerPorSiglaAbreviatura(String siglaAbreviatura);

    Sigla crear(Sigla sigla);

    Sigla actualizar(Long siglaId, Sigla sigla);

    void eliminar(Long siglaId);

    Optional<Sigla> obtenerOCrearPorAbreviatura(String siglaAbreviatura);
}
