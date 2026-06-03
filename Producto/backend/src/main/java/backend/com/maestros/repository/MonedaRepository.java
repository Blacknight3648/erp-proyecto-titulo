package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonedaRepository extends JpaRepository<Moneda, Integer> {

    Optional<Moneda> findByCodigoMoneda(String codigoMoneda);

    boolean existsByCodigoMoneda(String codigoMoneda);
}
