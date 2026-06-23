package backend.com.produccion.entity;

import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.produccion.infrastructure.persistence.entity.CosteoVersionJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.HojaCompraItemJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.HojaCompraJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("Hoja de Compra JPA (persistencia)")
class HojaCompraJpaEntityTest {

    @Nested
    @DisplayName("Inicialización y estructura")
    class Inicializacion {

        @Test
        @DisplayName("la lista de ítems debe estar inicializada por defecto y no ser nula")
        void listaItemsInicializada() {
            HojaCompraJpaEntity hojaCompra = new HojaCompraJpaEntity();

            // Verificamos que la colección no sea null y empiece vacía
            assertThat(hojaCompra.getItems())
                    .isNotNull()
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("debe asignar y recuperar todos los campos y relaciones correctamente")
        void mapeaCamposCorrectamente() {
            // Arrange
            HojaCompraJpaEntity hojaCompra = new HojaCompraJpaEntity();

            // Usamos mocks rápidos para las entidades relacionadas (evita instanciar objetos complejos)
            OrdenProduccionJpaEntity ordenProduccionMock = mock(OrdenProduccionJpaEntity.class);
            CosteoVersionJpaEntity costeoVersionMock = mock(CosteoVersionJpaEntity.class);
            List<HojaCompraItemJpaEntity> itemsSimulados = new ArrayList<>();

            Long idHC = 10L;
            String numeroHC = "HC-2026-0099";
            EstadoHC estado = EstadoHC.BORRADOR;
            LocalDate fechaGeneracion = LocalDate.now();
            String observaciones = "Observaciones de la hoja de compra de prueba";

            // Act
            hojaCompra.setIdHC(idHC);
            hojaCompra.setNumeroHC(numeroHC);
            hojaCompra.setOrdenProduccion(ordenProduccionMock);
            hojaCompra.setCosteoVersion(costeoVersionMock);
            hojaCompra.setEstado(estado);
            hojaCompra.setFechaGeneracion(fechaGeneracion);
            hojaCompra.setObservaciones(observaciones);
            hojaCompra.setItems(itemsSimulados);

            // Assert
            assertThat(hojaCompra.getIdHC()).isEqualTo(idHC);
            assertThat(hojaCompra.getNumeroHC()).isEqualTo(numeroHC);
            assertThat(hojaCompra.getOrdenProduccion()).isSameAs(ordenProduccionMock);
            assertThat(hojaCompra.getCosteoVersion()).isSameAs(costeoVersionMock);
            assertThat(hojaCompra.getEstado()).isEqualTo(estado);
            assertThat(hojaCompra.getFechaGeneracion()).isEqualTo(fechaGeneracion);
            assertThat(hojaCompra.getObservaciones()).isEqualTo(observaciones);
            assertThat(hojaCompra.getItems()).isSameAs(itemsSimulados);
        }
    }
}