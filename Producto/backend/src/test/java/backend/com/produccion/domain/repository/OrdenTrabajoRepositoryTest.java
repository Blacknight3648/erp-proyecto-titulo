package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.enums.EstadoOT;
import backend.com.produccion.domain.enums.FaseProduccion;
import backend.com.produccion.domain.enums.TipoOT;
import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.infrastructure.mapper.OrdenTrabajoMapper;
import backend.com.produccion.infrastructure.persistence.adapter.OrdenTrabajoRepositoryImpl;
import backend.com.produccion.infrastructure.persistence.entity.OrdenTrabajoJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({OrdenTrabajoRepositoryImpl.class, OrdenTrabajoMapper.class})
@DisplayName("Pruebas para OrdenTrabajoRepository")
class OrdenTrabajoRepositoryTest {

    @Autowired
    private OrdenTrabajoRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrdenTrabajoMapper mapper;

    // --- MÉTODOS REUTILIZABLES (HELPERS) ---
    private OrdenTrabajo crearOrdenTrabajoValida(Long notaVentaId, Long ordenProduccionId) {
        return new OrdenTrabajo(
                null,
                notaVentaId,
                1L,
                ordenProduccionId,
                1,
                TipoOT.INTERNA,
                FaseProduccion.CORTE,
                EstadoOT.PENDIENTE,
                100,
                0,
                0,
                "Observaciones de prueba"
        );
    }

    private OrdenTrabajoJpaEntity persistirOrdenTrabajo(Long notaVentaId, Long ordenProduccionId) {
        OrdenTrabajo ot = crearOrdenTrabajoValida(notaVentaId, ordenProduccionId);
        OrdenTrabajoJpaEntity entity = mapper.toJpaEntity(ot);
        return entityManager.persistAndFlush(entity);
    }

    // --- CASOS DE PRUEBA ORGANIZADOS ---

    @Nested
    @DisplayName("Pruebas para guardar órdenes (save)")
    class SaveTests {

        @Test
        @DisplayName("Debe persistir una nueva orden de trabajo correctamente")
        void debeGuardarOrdenTrabajo() {
            // Given
            OrdenTrabajo nuevaOt = crearOrdenTrabajoValida(100L, 200L);

            // When
            OrdenTrabajo otGuardada = repository.save(nuevaOt);

            // Then
            assertThat(otGuardada).isNotNull();
            assertThat(otGuardada.getIdOT()).isNotNull();
            assertThat(otGuardada.getNotaVentaId()).isEqualTo(100L);
            assertThat(otGuardada.getOrdenProduccionId()).isEqualTo(200L);
        }
    }

    @Nested
    @DisplayName("Pruebas para búsquedas por ID (findById)")
    class FindByIdTests {

        @Test
        @DisplayName("Debe encontrar la orden cuando el ID existe")
        void debeEncontrarPorId() {
            // Given
            OrdenTrabajoJpaEntity otEntity = persistirOrdenTrabajo(10L, 20L);

            // When
            Optional<OrdenTrabajo> encontrado = repository.findById(otEntity.getIdOT());

            // Then
            assertThat(encontrado).isPresent();
            assertThat(encontrado.get().getIdOT()).isEqualTo(otEntity.getIdOT());
        }

        @Test
        @DisplayName("Debe retornar un Optional vacío cuando el ID no existe")
        void noDebeEncontrarIdInexistente() {
            // When
            Optional<OrdenTrabajo> encontrado = repository.findById(999L);

            // Then
            assertThat(encontrado).isEmpty();
        }
    }

    @Nested
    @DisplayName("Pruebas para búsquedas por Nota de Venta (findByNotaVentaId)")
    class FindByNotaVentaTests {

        @Test
        @DisplayName("Debe retornar la lista de órdenes asociadas a una Nota de Venta")
        void debeEncontrarPorNotaVentaId() {
            // Given
            Long notaVentaTargetId = 500L;
            OrdenTrabajoJpaEntity otEntity1 = persistirOrdenTrabajo(notaVentaTargetId, 1L);
            OrdenTrabajoJpaEntity otEntity2 = persistirOrdenTrabajo(notaVentaTargetId, 2L);
            OrdenTrabajoJpaEntity otDeOtraNotaEntity = persistirOrdenTrabajo(999L, 3L);

            // When
            List<OrdenTrabajo> resultado = repository.findByNotaVentaId(notaVentaTargetId);

            // Then
            assertThat(resultado).hasSize(2);
            assertThat(resultado).extracting(OrdenTrabajo::getIdOT)
                    .containsExactlyInAnyOrder(otEntity1.getIdOT(), otEntity2.getIdOT());
            assertThat(resultado).extracting(OrdenTrabajo::getIdOT)
                    .doesNotContain(otDeOtraNotaEntity.getIdOT());
        }

        @Test
        @DisplayName("Debe retornar una lista vacía si ninguna orden coincide con la Nota de Venta")
        void debeRetornarVacioSiNoHayNotaVenta() {
            // When
            List<OrdenTrabajo> resultado = repository.findByNotaVentaId(777L);

            // Then
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("Pruebas para búsquedas por Orden de Producción (findByOrdenProduccionId)")
    class FindByOrdenProduccionTests {

        @Test
        @DisplayName("Debe retornar la lista de órdenes asociadas a una Orden de Producción")
        void debeEncontrarPorOrdenProduccionId() {
            // Given
            Long ordenProdTargetId = 800L;
            OrdenTrabajoJpaEntity otEntity1 = persistirOrdenTrabajo(1L, ordenProdTargetId);
            OrdenTrabajoJpaEntity otEntity2 = persistirOrdenTrabajo(2L, ordenProdTargetId);

            // When
            List<OrdenTrabajo> resultado = repository.findByOrdenProduccionId(ordenProdTargetId);

            // Then
            assertThat(resultado).hasSize(2);
            assertThat(resultado).extracting(OrdenTrabajo::getOrdenProduccionId)
                    .containsOnly(ordenProdTargetId);
        }
    }
}
