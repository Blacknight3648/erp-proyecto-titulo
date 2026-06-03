package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.catalogo.GramajeTela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GramajeTelaRepository extends JpaRepository<GramajeTela, Integer> {

    Optional<GramajeTela> findByCodigoGramaje(String codigoGramaje);

    boolean existsByCodigoGramaje(String codigoGramaje);

    /** Gramajes asociados a una categoría de vestuario (ej: "Camisería", "Abrigo"). */
    List<GramajeTela> findByCategoriaVestuarioIgnoreCase(String categoriaVestuario);
}
