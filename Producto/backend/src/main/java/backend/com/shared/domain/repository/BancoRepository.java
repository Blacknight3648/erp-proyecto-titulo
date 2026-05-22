package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BancoRepository
        extends JpaRepository<Banco, Integer> {
}
