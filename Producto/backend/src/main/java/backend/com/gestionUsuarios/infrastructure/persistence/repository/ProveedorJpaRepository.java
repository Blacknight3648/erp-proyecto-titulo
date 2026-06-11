package backend.com.gestionUsuarios.proveedor.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorJpaRepository extends JpaRepository<ProveedorJpaEntity, Long> {

    Optional<ProveedorJpaEntity> findByRunProveedor(String runProveedor);

    boolean existsByRunProveedor(String runProveedor);

    @Query("SELECT p FROM ProveedorJpaEntity p WHERE LOWER(p.razonSocialProveedor) LIKE LOWER(CONCAT('%', :razonSocial, '%'))")
    List<ProveedorJpaEntity> buscarPorRazonSocial(@Param("razonSocial") String razonSocial);

    @Query("SELECT p FROM ProveedorJpaEntity p WHERE LOWER(p.sigla) = LOWER(:sigla)")
    List<ProveedorJpaEntity> obtenerPorSigla(@Param("sigla") String sigla);

    @Query("SELECT p FROM ProveedorJpaEntity p WHERE p.giro.giroId = :giroId")
    List<ProveedorJpaEntity> obtenerPorGiroId(@Param("giroId") Long giroId);

    @Query("SELECT p FROM ProveedorJpaEntity p WHERE LOWER(p.giro.descripcionGiro) = LOWER(:descripcionGiro)")
    List<ProveedorJpaEntity> obtenerPorDescripcionGiro(@Param("descripcionGiro") String descripcionGiro);

    List<ProveedorJpaEntity> findByActivoTrue();

    List<ProveedorJpaEntity> findByActivoFalse();

    @Query("SELECT p FROM ProveedorJpaEntity p WHERE p.activo = true AND LOWER(p.sigla) = LOWER(:sigla)")
    List<ProveedorJpaEntity> obtenerActivosPorSigla(@Param("sigla") String sigla);

    @Query("SELECT p FROM ProveedorJpaEntity p WHERE p.activo = true AND p.giro.giroId = :giroId")
    List<ProveedorJpaEntity> obtenerActivosPorGiro(@Param("giroId") Long giroId);
}
