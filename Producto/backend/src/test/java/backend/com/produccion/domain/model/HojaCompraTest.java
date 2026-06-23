package backend.com.produccion.domain.model;

import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests unitarios PUROS del modelo de dominio para HojaCompra.
 * Al igual que OrdenTrabajo, sirven como documentación viva del negocio.
 */
@DisplayName("Hoja de Compra (dominio)")
class HojaCompraTest {

    // Helper para crear un borrador base de forma rápida
    private HojaCompra nuevaHojaCompraBorrador() {
        return HojaCompra.crearBorrador(
                new DocumentNumber(12345L),
                1L, // opId
                1L // costeoVersionId
        );
    }

    // Helper para crear un item de hoja de compra válido
    private HojaCompraItem nuevoHojaCompraItem() {
        return new HojaCompraItem(
                1L,
                1L,
                "MATERIA_PRIMA",
                101,
                "Insumo de prueba",
                BigDecimal.valueOf(2.5),
                10,
                BigDecimal.valueOf(25.0),
                BigDecimal.valueOf(100.0));
    }

    @Nested
    @DisplayName("Creación y Constructor")
    class Creacion {

        @Test
        @DisplayName("un borrador recién creado tiene estado BORRADOR, fecha de hoy y lista de ítems vacía")
        void creadorBorradorCorrecto() {
            HojaCompra hc = nuevaHojaCompraBorrador();

            assertThat(hc.getEstado()).isEqualTo(EstadoHC.BORRADOR);
            assertThat(hc.getFechaGeneracion()).isEqualTo(LocalDate.now());
            assertThat(hc.getItems()).isEmpty();
            assertThat(hc.getObservaciones()).isNull();
        }

        @Test
        @DisplayName("el constructor completo asigna valores por defecto si el estado o fecha son nulos")
        void constructorAsignaValoresPorDefecto() {
            List<HojaCompraItem> itemsIniciales = new ArrayList<>();

            HojaCompra hc = new HojaCompra(
                    1L, new DocumentNumber(12345L), 1L, 1L,
                    null, null, "Obs de prueba", itemsIniciales);

            assertThat(hc.getEstado()).isEqualTo(EstadoHC.BORRADOR);
            assertThat(hc.getFechaGeneracion()).isEqualTo(LocalDate.now());
            assertThat(hc.getObservaciones()).isEqualTo("Obs de prueba");
        }

        @Test
        @DisplayName("el constructor completo copia los ítems iniciales si no son nulos")
        void constructorCopiaItemsIniciales() {
            List<HojaCompraItem> itemsIniciales = new ArrayList<>();
            itemsIniciales.add(nuevoHojaCompraItem());

            HojaCompra hc = new HojaCompra(
                    1L, new DocumentNumber(12345L), 1L, 1L,
                    EstadoHC.BORRADOR, LocalDate.now(), "Obs de prueba", itemsIniciales);

            assertThat(hc.getItems()).hasSize(1).containsExactlyElementsOf(itemsIniciales);
        }
    }

    @Nested
    @DisplayName("Transiciones de Estado")
    class TransicionesEstado {

        @Test
        @DisplayName("permite aprobar una Hoja de Compra si está en un estado válido")
        void apruebaHojaCompraExitosamente() {
            HojaCompra hc = nuevaHojaCompraBorrador();

            hc.aprobar();

            assertThat(hc.getEstado()).isEqualTo(EstadoHC.APROBADA);
        }

        @Test
        @DisplayName("permite cerrar una Hoja de Compra si está en un estado válido")
        void cierraHojaCompraExitosamente() {
            HojaCompra hc = nuevaHojaCompraBorrador();

            hc.aprobar();
            hc.cerrar();

            assertThat(hc.getEstado()).isEqualTo(EstadoHC.CERRADA);
        }

        @Test
        @DisplayName("lanza IllegalStateException si se intenta aprobar desde un estado no permitido")
        void rechazaAprobacionInvalida() {
            HojaCompra hc = nuevaHojaCompraBorrador();
            hc.aprobar(); // Ya está APROBADA. Volver a aprobar debería fallar.

            assertThatThrownBy(hc::aprobar)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No se puede aprobar una HC en estado APROBADA");
        }

        @Test
        @DisplayName("lanza IllegalStateException si se intenta cerrar directamente desde BORRADOR")
        void rechazaCierreInvalido() {
            HojaCompra hc = nuevaHojaCompraBorrador();

            assertThatThrownBy(hc::cerrar)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No se puede cerrar una HC en estado BORRADOR");
        }

        @Test
        @DisplayName("lanza IllegalStateException si se intenta operar sobre una HC que ya está CERRADA")
        void rechazaTransicionesDesdeCerrada() {
            HojaCompra hc = nuevaHojaCompraBorrador();
            hc.aprobar();
            hc.cerrar();

            assertThatThrownBy(hc::aprobar)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No se puede aprobar una HC en estado CERRADA");

            assertThatThrownBy(hc::cerrar)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No se puede cerrar una HC en estado CERRADA");
        }
    }

    @Nested
    @DisplayName("Gestión de Ítems")
    class GestionItems {

        @Test
        @DisplayName("permite agregar ítems a la hoja de compra")
        void agregaItemsCorrectamente() {
            HojaCompra hc = nuevaHojaCompraBorrador();
            HojaCompraItem item = nuevoHojaCompraItem();

            hc.addItem(item);

            assertThat(hc.getItems()).hasSize(1).contains(item);
        }

        @Test
        @DisplayName("la lista devuelta por getItems() es inmutable y lanza excepción si se intenta modificar externamente")
        void listaDeItemsEsInmutable() {
            HojaCompra hc = nuevaHojaCompraBorrador();
            HojaCompraItem item = nuevoHojaCompraItem();

            List<HojaCompraItem> itemsDeLaHc = hc.getItems();

            assertThatThrownBy(() -> itemsDeLaHc.add(item))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}