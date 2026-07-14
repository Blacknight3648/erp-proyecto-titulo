package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.domain.enums.TipoArticulo;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ArticuloJpaRepository extends JpaRepository<ArticuloJpaEntity, Integer> {

    Optional<ArticuloJpaEntity> findByCodigoArticulo(String codigoArticulo);

    boolean existsByCodigoArticulo(String codigoArticulo);

    List<ArticuloJpaEntity> findByActivoTrue();

    // Navega la asociación @ManyToOne 'tipoArticulo' hasta su atributo 'codigo'
    // (de tipo TipoArticulo). El guion bajo es el separador explícito de Spring Data
    // para atravesar la relación; sin él, intentaría comparar la entidad relacional
    // con el enum y lanzaría InvalidDataAccessApiUsageException.
    List<ArticuloJpaEntity> findByTipoArticulo_Codigo(TipoArticulo tipoArticulo);

    List<ArticuloJpaEntity> findByNombreArticuloContainingIgnoreCase(String nombre);
}