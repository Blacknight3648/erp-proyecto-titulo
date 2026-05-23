package backend.com.gestionUsuarios.cliente.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {

    Optional<ClienteJpaEntity> findByRunCliente(String runCliente);

    boolean existsByRunCliente(String runCliente);

    @Query("SELECT c FROM ClienteJpaEntity c WHERE LOWER(c.razonSocial) LIKE LOWER(CONCAT('%', :razonSocial, '%'))")
    List<ClienteJpaEntity> buscarPorRazonSocial(@Param("razonSocial") String razonSocial);

    @Query("SELECT c FROM ClienteJpaEntity c WHERE LOWER(c.sigla) = LOWER(:sigla)")
    List<ClienteJpaEntity> obtenerPorSigla(@Param("sigla") String sigla);

    @Query("SELECT c FROM ClienteJpaEntity c WHERE c.giro.giroId = :giroId")
    List<ClienteJpaEntity> obtenerPorGiroId(@Param("giroId") Long giroId);

    @Query("SELECT c FROM ClienteJpaEntity c WHERE LOWER(c.giro.descripcionGiro) = LOWER(:descripcionGiro)")
    List<ClienteJpaEntity> obtenerPorDescripcionGiro(@Param("descripcionGiro") String descripcionGiro);

    List<ClienteJpaEntity> findByActivoTrue();

    List<ClienteJpaEntity> findByActivoFalse();

    @Query("SELECT c FROM ClienteJpaEntity c WHERE c.activo = true AND LOWER(c.sigla) = LOWER(:sigla)")
    List<ClienteJpaEntity> obtenerActivosPorSigla(@Param("sigla") String sigla);

    @Query("SELECT c FROM ClienteJpaEntity c WHERE c.activo = true AND c.giro.giroId = :giroId")
    List<ClienteJpaEntity> obtenerActivosPorGiro(@Param("giroId") Long giroId);
}
