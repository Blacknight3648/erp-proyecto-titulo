package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BancoJpaRepository extends JpaRepository<Banco, Integer> {
    Optional<Banco> findByCodigoBanco(String codigoBanco);
}
