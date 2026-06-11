package backend.com.produccion.domain.model;

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
 * Tests unitarios PUROS del modelo de dominio para RecepcionOC.
 */
@DisplayName("RecepcionOC (dominio)")
class RecepcionOCTest {

    private RecepcionOCItem nuevoItem() {
        return new RecepcionOCItem(1L, 1L, 1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO, null);
    }

    @Nested
    @DisplayName("Constructor")
    class Constructor {

        @Test
        @DisplayName("asigna todos los valores recibidos correctamente")
        void asignaValoresCorrectamente() {
            LocalDate fecha = LocalDate.of(2025, 1, 15);

            RecepcionOC recepcion = new RecepcionOC(1L, 10L, fecha, "G-001", "Juan Perez", "Sin novedad", new ArrayList<>());

            assertThat(recepcion.getIdRecepcion()).isEqualTo(1L);
            assertThat(recepcion.getOcId()).isEqualTo(10L);
            assertThat(recepcion.getFechaRecepcion()).isEqualTo(fecha);
            assertThat(recepcion.getNumeroGuia()).isEqualTo("G-001");
            assertThat(recepcion.getResponsable()).isEqualTo("Juan Perez");
            assertThat(recepcion.getObservaciones()).isEqualTo("Sin novedad");
            assertThat(recepcion.getItems()).isEmpty();
        }

        @Test
        @DisplayName("asigna la fecha de hoy si fechaRecepcion es nula")
        void asignaFechaActualSiEsNula() {
            RecepcionOC recepcion = new RecepcionOC(1L, 10L, null, "G-001", "Juan", null, null);

            assertThat(recepcion.getFechaRecepcion()).isEqualTo(LocalDate.now());
        }

        @Test
        @DisplayName("copia los items iniciales si no son nulos")
        void copiaItemsIniciales() {
            List<RecepcionOCItem> itemsIniciales = new ArrayList<>();
            itemsIniciales.add(nuevoItem());

            RecepcionOC recepcion = new RecepcionOC(1L, 10L, LocalDate.now(), "G-001", "Juan", null, itemsIniciales);

            assertThat(recepcion.getItems()).hasSize(1).containsExactlyElementsOf(itemsIniciales);
        }
    }

    @Nested
    @DisplayName("Factory: crear")
    class Crear {

        @Test
        @DisplayName("crea una recepción sin id, con fecha de hoy y lista de items vacía")
        void creaRecepcionCorrectamente() {
            RecepcionOC recepcion = RecepcionOC.crear(10L, null, "G-002", "Pedro", "Obs");

            assertThat(recepcion.getIdRecepcion()).isNull();
            assertThat(recepcion.getOcId()).isEqualTo(10L);
            assertThat(recepcion.getFechaRecepcion()).isEqualTo(LocalDate.now());
            assertThat(recepcion.getNumeroGuia()).isEqualTo("G-002");
            assertThat(recepcion.getResponsable()).isEqualTo("Pedro");
            assertThat(recepcion.getObservaciones()).isEqualTo("Obs");
            assertThat(recepcion.getItems()).isEmpty();
        }

        @Test
        @DisplayName("respeta la fecha indicada si no es nula")
        void respetaFechaIndicada() {
            LocalDate fecha = LocalDate.of(2025, 5, 1);

            RecepcionOC recepcion = RecepcionOC.crear(10L, fecha, "G-002", "Pedro", "Obs");

            assertThat(recepcion.getFechaRecepcion()).isEqualTo(fecha);
        }
    }

    @Nested
    @DisplayName("Gestión de Ítems")
    class GestionItems {

        @Test
        @DisplayName("permite agregar ítems a la recepción")
        void agregaItemsCorrectamente() {
            RecepcionOC recepcion = RecepcionOC.crear(10L, null, "G-001", "Juan", null);
            RecepcionOCItem item = nuevoItem();

            recepcion.addItem(item);

            assertThat(recepcion.getItems()).hasSize(1).contains(item);
        }

        @Test
        @DisplayName("la lista devuelta por getItems() es inmutable")
        void listaDeItemsEsInmutable() {
            RecepcionOC recepcion = RecepcionOC.crear(10L, null, "G-001", "Juan", null);
            List<RecepcionOCItem> items = recepcion.getItems();

            assertThatThrownBy(() -> items.add(nuevoItem()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
