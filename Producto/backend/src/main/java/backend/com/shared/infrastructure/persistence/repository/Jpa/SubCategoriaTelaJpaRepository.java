package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.SubCategoriaTelaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubCategoriaTelaJpaRepository extends JpaRepository<SubCategoriaTelaJpaEntity, Integer> {

    Optional<SubCategoriaTelaJpaEntity> findByCodigoSubCategoriaTela(String codigoSubCategoriaTela);

    boolean existsByCodigoSubCategoriaTela(String codigoSubCategoriaTela);

    List<SubCategoriaTelaJpaEntity> findByCategoriaTela_IdCategoriaTela(Integer idCategoriaTela);
}
