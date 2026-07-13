package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Modelo;

import java.util.List;
import java.util.Optional;

public interface ModeloRepository {

    List<Modelo> findAll();

    Optional<Modelo> findById(Integer id);

    Optional<Modelo> findByCodigoModelo(String codigoModelo);

    Optional<Modelo> findByNombreModelo(String nombreModelo);

    boolean existsByCodigoModelo(String codigoModelo);

    boolean existsByNombreModelo(String nombreModelo);

    boolean existsById(Integer id);

    Modelo save(Modelo modelo);

    void deleteById(Integer id);
}
