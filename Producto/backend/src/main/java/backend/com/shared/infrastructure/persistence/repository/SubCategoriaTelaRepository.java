package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.SubCategoriaTela;

import java.util.List;
import java.util.Optional;

public interface SubCategoriaTelaRepository {

    List<SubCategoriaTela> findAll();

    Optional<SubCategoriaTela> findById(Integer id);

    Optional<SubCategoriaTela> findByCodigoSubCategoriaTela(String codigoSubCategoriaTela);

    boolean existsByCodigoSubCategoriaTela(String codigoSubCategoriaTela);

    boolean existsById(Integer id);

    List<SubCategoriaTela> findByCategoriaTelajId(Integer idCategoriaTela);

    SubCategoriaTela save(SubCategoriaTela subCategoriaTela);

    void deleteById(Integer id);
}
