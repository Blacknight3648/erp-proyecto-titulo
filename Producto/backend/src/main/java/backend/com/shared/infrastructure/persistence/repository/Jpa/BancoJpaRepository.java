package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.BancoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BancoJpaRepository extends JpaRepository<BancoJpaEntity, Integer> {
    Optional<BancoJpaEntity> findByCodigoBanco(String codigoBanco);
}
