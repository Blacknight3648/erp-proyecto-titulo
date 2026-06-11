package backend.com.produccion.application.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import backend.com.produccion.infrastructure.persistence.entity.CosteoItemJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.CosteoJpaEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CosteoJpaEntity (persistencia)")
class CosteoJpaEntityTest {

    @Nested
    @DisplayName("Inicialización y Estructura")
    class Inicializacion {

        @Test
        @DisplayName("la lista de ítems debe estar inicializada por defecto y no ser nula")
        void listaItemsInicializada() {
            CosteoJpaEntity costeo = new CosteoJpaEntity();

            // Corrección aquí: comprobamos que NO es nula y que está vacía ([])
            assertThat(costeo.getItems())
                    .isNotNull()
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("debe asignar y recuperar todos los campos correctamente")
        void mapeaCamposCorrectamente() {
            // Arrange
            CosteoJpaEntity costeo = new CosteoJpaEntity();
            List<CosteoItemJpaEntity> itemsSimulados = new ArrayList<>();

            Long idCosteo = 1L;
            Long solicitudCostosId = 100L;
            String numeroCosteo = "C-2026-001";
            BigDecimal costoHilos = new BigDecimal("150.50");
            BigDecimal costoManoObra = new BigDecimal("1200.00");
            BigDecimal costoEtiquetas = new BigDecimal("45.20");
            BigDecimal costoEmbalaje = new BigDecimal("80.00");
            BigDecimal costoFlete = new BigDecimal("350.00");
            BigDecimal porcentajeCostoFijo = new BigDecimal("10.00");
            BigDecimal precioCinta1 = new BigDecimal("12.50");
            BigDecimal cantidadCinta1 = new BigDecimal("5.5000");
            BigDecimal precioCinta2 = new BigDecimal("14.00");
            BigDecimal cantidadCinta2 = new BigDecimal("2.3000");
            BigDecimal vivoReflectivo = new BigDecimal("25.00");
            BigDecimal cantidadVivo = new BigDecimal("1.5000");
            BigDecimal costoTotalMateriaPrima = new BigDecimal("2500.75");
            BigDecimal margenBrutoSugerido = new BigDecimal("35.00");
            BigDecimal precioVentaSugerido = new BigDecimal("3850.00");

            // Act
            costeo.setIdCosteo(idCosteo);
            costeo.setSolicitudCostosId(solicitudCostosId);
            costeo.setItems(itemsSimulados);
            costeo.setNumeroCosteo(numeroCosteo);
            costeo.setCostoHilos(costoHilos);
            costeo.setCostoManoObra(costoManoObra);
            costeo.setCostoEtiquetas(costoEtiquetas);
            costeo.setCostoEmbalaje(costoEmbalaje);
            costeo.setCostoFlete(costoFlete);
            costeo.setPorcentajeCostoFijo(porcentajeCostoFijo);
            costeo.setPrecioCinta1(precioCinta1);
            costeo.setCantidadCinta1(cantidadCinta1);
            costeo.setPrecioCinta2(precioCinta2);
            costeo.setCantidadCinta2(cantidadCinta2);
            costeo.setVivoReflectivo(vivoReflectivo);
            costeo.setCantidadVivo(cantidadVivo);
            costeo.setCostoTotalMateriaPrima(costoTotalMateriaPrima);
            costeo.setMargenBrutoSugerido(margenBrutoSugerido);
            costeo.setPrecioVentaSugerido(precioVentaSugerido);

            // Assert
            assertThat(costeo.getIdCosteo()).isEqualTo(idCosteo);
            assertThat(costeo.getSolicitudCostosId()).isEqualTo(solicitudCostosId);
            assertThat(costeo.getItems()).isSameAs(itemsSimulados);
            assertThat(costeo.getNumeroCosteo()).isEqualTo(numeroCosteo);

            // Verificaciones de BigDecimals
            assertThat(costeo.getCostoHilos()).isEqualByComparingTo(costoHilos);
            assertThat(costeo.getCostoManoObra()).isEqualByComparingTo(costoManoObra);
            assertThat(costeo.getCostoEtiquetas()).isEqualByComparingTo(costoEtiquetas);
            assertThat(costeo.getCostoEmbalaje()).isEqualByComparingTo(costoEmbalaje);
            assertThat(costeo.getCostoFlete()).isEqualByComparingTo(costoFlete);
            assertThat(costeo.getPorcentajeCostoFijo()).isEqualByComparingTo(porcentajeCostoFijo);
            assertThat(costeo.getPrecioCinta1()).isEqualByComparingTo(precioCinta1);
            assertThat(costeo.getCantidadCinta1()).isEqualByComparingTo(cantidadCinta1);
            assertThat(costeo.getPrecioCinta2()).isEqualByComparingTo(precioCinta2);
            assertThat(costeo.getCantidadCinta2()).isEqualByComparingTo(cantidadCinta2);
            assertThat(costeo.getVivoReflectivo()).isEqualByComparingTo(vivoReflectivo);
            assertThat(costeo.getCantidadVivo()).isEqualByComparingTo(cantidadVivo);
            assertThat(costeo.getCostoTotalMateriaPrima()).isEqualByComparingTo(costoTotalMateriaPrima);
            assertThat(costeo.getMargenBrutoSugerido()).isEqualByComparingTo(margenBrutoSugerido);
            assertThat(costeo.getPrecioVentaSugerido()).isEqualByComparingTo(precioVentaSugerido);
        }
    }
}
