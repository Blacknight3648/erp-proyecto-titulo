package backend.com.produccion.domain.model;

import backend.com.produccion.domain.enums.EstadoOC;
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
 * Tests unitarios PUROS del modelo de dominio para OrdenCompra.
 */
@DisplayName("OrdenCompra (dominio)")
class OrdenCompraTest {

    private OrdenCompra nuevaOCEmitida() {
        return OrdenCompra.emitir(new DocumentNumber(1001L), 1L, LocalDate.now().plusDays(7), "Obs");
    }

    private OrdenCompraItem nuevoItem(BigDecimal cantidadComprada, BigDecimal precioUnitario) {
        return new OrdenCompraItem(null, null, "MATERIA_PRIMA", 1, "Tela", cantidadComprada,
                BigDecimal.ZERO, cantidadComprada, precioUnitario, null, new ArrayList<>());
    }

    @Nested
    @DisplayName("Creación")
    class Creacion {

        @Test
        @DisplayName("emitir() crea una OC en estado EMITIDA, fecha de hoy, total cero y sin ítems")
        void emitirCreaOCCorrecta() {
            OrdenCompra oc = nuevaOCEmitida();

            assertThat(oc.getEstado()).isEqualTo(EstadoOC.EMITIDA);
            assertThat(oc.getFechaEmision()).isEqualTo(LocalDate.now());
            assertThat(oc.getTotalNeto()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(oc.getItems()).isEmpty();
        }

        @Test
        @DisplayName("el constructor asigna valores por defecto si estado, fecha y totalNeto son nulos")
        void constructorAsignaValoresPorDefecto() {
            OrdenCompra oc = new OrdenCompra(1L, new DocumentNumber(1001L), 1L, null, null, null, "Obs", null, null);

            assertThat(oc.getEstado()).isEqualTo(EstadoOC.EMITIDA);
            assertThat(oc.getFechaEmision()).isEqualTo(LocalDate.now());
            assertThat(oc.getTotalNeto()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(oc.getItems()).isEmpty();
        }

        @Test
        @DisplayName("el constructor copia los ítems iniciales si no son nulos")
        void constructorCopiaItemsIniciales() {
            List<OrdenCompraItem> itemsIniciales = new ArrayList<>();
            itemsIniciales.add(nuevoItem(BigDecimal.TEN, BigDecimal.ONE));

            OrdenCompra oc = new OrdenCompra(1L, new DocumentNumber(1001L), 1L, EstadoOC.EMITIDA,
                    LocalDate.now(), null, "Obs", null, itemsIniciales);

            assertThat(oc.getItems()).hasSize(1).containsExactlyElementsOf(itemsIniciales);
        }
    }

    @Nested
    @DisplayName("Gestión de Ítems")
    class GestionItems {

        @Test
        @DisplayName("addItem agrega el ítem y recalcula el total neto sumando los subtotales")
        void addItemRecalculaTotal() {
            OrdenCompra oc = nuevaOCEmitida();

            oc.addItem(nuevoItem(BigDecimal.TEN, BigDecimal.valueOf(2)));
            oc.addItem(nuevoItem(BigDecimal.valueOf(5), BigDecimal.valueOf(3)));

            assertThat(oc.getItems()).hasSize(2);
            assertThat(oc.getTotalNeto()).isEqualByComparingTo(BigDecimal.valueOf(35)); // (10*2) + (5*3)
        }

        @Test
        @DisplayName("la lista devuelta por getItems() es inmutable")
        void listaDeItemsEsInmutable() {
            OrdenCompra oc = nuevaOCEmitida();
            List<OrdenCompraItem> items = oc.getItems();

            assertThatThrownBy(() -> items.add(nuevoItem(BigDecimal.ONE, BigDecimal.ONE)))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("Transiciones de Estado")
    class TransicionesEstado {

        @Test
        @DisplayName("permite la secuencia completa EMITIDA -> ENVIADA -> RECEPCIONADA -> CERRADA")
        void permiteSecuenciaCompleta() {
            OrdenCompra oc = nuevaOCEmitida();

            oc.marcarEnviada();
            assertThat(oc.getEstado()).isEqualTo(EstadoOC.ENVIADA);

            oc.marcarRecepcionada();
            assertThat(oc.getEstado()).isEqualTo(EstadoOC.RECEPCIONADA);

            oc.cerrar();
            assertThat(oc.getEstado()).isEqualTo(EstadoOC.CERRADA);
        }

        @Test
        @DisplayName("permite recepción parcial desde ENVIADA y luego pasar a RECEPCIONADA")
        void permiteRecepcionParcial() {
            OrdenCompra oc = nuevaOCEmitida();
            oc.marcarEnviada();

            oc.marcarRecepcionParcial();
            assertThat(oc.getEstado()).isEqualTo(EstadoOC.RECEPCIONADA_PARCIAL);

            oc.marcarRecepcionada();
            assertThat(oc.getEstado()).isEqualTo(EstadoOC.RECEPCIONADA);
        }

        @Test
        @DisplayName("rechaza marcar como enviada una OC que no está EMITIDA")
        void rechazaEnviarDesdeEstadoInvalido() {
            OrdenCompra oc = nuevaOCEmitida();
            oc.marcarEnviada();

            assertThatThrownBy(oc::marcarEnviada)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Transición inválida de OC");
        }

        @Test
        @DisplayName("rechaza marcar recepción directamente desde EMITIDA")
        void rechazaRecepcionDesdeEmitida() {
            OrdenCompra oc = nuevaOCEmitida();

            assertThatThrownBy(oc::marcarRecepcionada)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Transición inválida de OC");
        }

        @Test
        @DisplayName("rechaza cerrar una OC que no está RECEPCIONADA")
        void rechazaCerrarDesdeEstadoInvalido() {
            OrdenCompra oc = nuevaOCEmitida();

            assertThatThrownBy(oc::cerrar)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Transición inválida de OC");
        }

        @Test
        @DisplayName("rechaza cualquier transición desde CERRADA")
        void rechazaTransicionesDesdeCerrada() {
            OrdenCompra oc = nuevaOCEmitida();
            oc.marcarEnviada();
            oc.marcarRecepcionada();
            oc.cerrar();

            assertThatThrownBy(oc::marcarEnviada).isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(oc::cerrar).isInstanceOf(IllegalStateException.class);
        }
    }
}
