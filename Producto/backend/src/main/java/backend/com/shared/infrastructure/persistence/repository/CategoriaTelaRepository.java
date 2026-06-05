package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.CategoriaTela;

import java.util.List;
import java.util.Optional;

public interface CategoriaTelaRepository {

    List<CategoriaTela> findAll();

    Optional<CategoriaTela> findById(Integer id);

    Optional<CategoriaTela> findByCodigoCategoriaTela(String codigoCategoriaTela);

    Optional<CategoriaTela> findByNombreCategoriaTela(String nombreCategoriaTela);

    boolean existsByCodigoCategoriaTela(String codigoCategoriaTela);

    boolean existsByNombreCategoriaTela(String nombreCategoriaTela);

    boolean existsById(Integer id);

    CategoriaTela save(CategoriaTela categoriaTela);

    void deleteById(Integer id);
}
