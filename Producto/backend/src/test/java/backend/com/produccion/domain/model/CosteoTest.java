package backend.com.produccion.domain.model;

import backend.com.produccion.domain.enums.EstadoCosteo;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests unitarios PUROS del modelo de dominio Costeo.
 * No requieren Spring ni entorno de base de datos: ejecutan en milisegundos.
 * * Sirve como documentación viva de cómo se estructura un Costeo y sus invariantes básicas.
 */
@DisplayName("Costeo (dominio)")
class CosteoTest {

    // --- INSTANCIADORES REUTILIZABLES (HELPERS) ---
    private Money mockMoney(String value) {
        // Ajusta el segundo parámetro según lo que pida tu constructor real (ej: "USD", "CLP", o un Enum)
        return new Money(new BigDecimal(value), "USD");
    }

    private DocumentNumber mockDocumentNumber(Long value) {
        return new DocumentNumber(value);
    }

    @Nested
    @DisplayName("Creación mediante Constructor Completo")
    class CreacionConstructor {

        @Test
        @DisplayName("debe instanciar todas las propiedades correctamente al pasar parámetros válidos")
        void debeInstanciarCorrectamente() {
            // Given
            List<CosteoItem> itemsDePrueba = new ArrayList<>();
            itemsDePrueba.add(new CosteoItem()); // Asumiendo existencia de la clase CosteoItem

            // When
            Costeo costeo = new Costeo(
                    1L,
                    mockDocumentNumber(1005L),
                    10L,
                    20L, "Cliente Textil S.A.",
                    30L, "Juan Pérez",
                    mockMoney("50.00"),   // costoHilos
                    mockMoney("150.00"),  // costoManoObra
                    mockMoney("10.00"),   // costoEtiquetas
                    mockMoney("15.00"),   // costoEmbalaje
                    mockMoney("35.00"),   // costoFlete
                    new BigDecimal("5.0"), // porcentajeCostoFijo
                    mockMoney("12.50"),   // precioCinta1
                    new BigDecimal("100"), // cantidadCinta1
                    mockMoney("8.00"),    // precioCinta2
                    new BigDecimal("50"),  // cantidadCinta2
                    mockMoney("22.00"),   // vivoReflectivo
                    new BigDecimal("30"),  // cantidadVivo
                    mockMoney("500.00"),  // costoTotalMateriaPrima
                    new BigDecimal("35.0"), // margenBrutoSugerido
                    mockMoney("675.00"),  // precioVentaSugerido
                    itemsDePrueba
            );

            // Then
            assertThat(costeo.getIdCosteo()).isEqualTo(1L);
            assertThat(costeo.getNumeroCosteo().getValue()).isEqualTo("1005"); // Ajustar según método de DocumentNumber
            assertThat(costeo.getClienteNombre()).isEqualTo("Cliente Textil S.A.");
            assertThat(costeo.getCostoHilos()).isEqualTo(mockMoney("50.00"));
            assertThat(costeo.getPorcentajeCostoFijo()).isEqualByComparingTo(new BigDecimal("5.0"));
            assertThat(costeo.getCantidadCinta1()).isEqualByComparingTo(new BigDecimal("100"));
            assertThat(costeo.getItems()).hasSize(1).containsAll(itemsDePrueba);
        }

        @Test
        @DisplayName("debe inicializar una lista vacía de items si se le pasa 'null' en el constructor")
        void debeInicializarListaVaciaCuandoItemsEsNull() {
            // When
            Costeo costeo = new Costeo(
                    1L, mockDocumentNumber(1L), 2L, 3L, "Cliente", 4L, "Vendedor",
                    mockMoney("0"), mockMoney("0"), mockMoney("0"), mockMoney("0"), mockMoney("0"),
                    BigDecimal.ZERO, mockMoney("0"), BigDecimal.ZERO, mockMoney("0"), BigDecimal.ZERO,
                    mockMoney("0"), BigDecimal.ZERO, mockMoney("0"), BigDecimal.ZERO, mockMoney("0"),
                    null // Lista de items explícitamente nula
            );

            // Then
            assertThat(costeo.getItems()).isNotNull().isEmpty();
        }
    }

    @Nested
    @DisplayName("Modificación vía Setters")
    class ModificacionSetters {

        @Test
        @DisplayName("debe mutar el estado de los componentes correctamente")
        void debeMutarCamposConSetters() {
            // Given
            Costeo costeo = new Costeo();

            // When
            costeo.setIdCosteo(99L);
            costeo.setClienteNombre("Nuevo Cliente");
            costeo.setCostoFlete(mockMoney("120.50"));
            costeo.setMargenBrutoSugerido(new BigDecimal("40.0"));

            // Then
            assertThat(costeo.getIdCosteo()).isEqualTo(99L);
            assertThat(costeo.getClienteNombre()).isEqualTo("Nuevo Cliente");
            assertThat(costeo.getCostoFlete()).isEqualTo(mockMoney("120.50"));
            assertThat(costeo.getMargenBrutoSugerido()).isEqualByComparingTo(new BigDecimal("40.0"));
        }
    }

    @Nested
    @DisplayName("Ciclo de vida (estados)")
    class CicloDeVida {

        @Test
        @DisplayName("un costeo nuevo arranca en BORRADOR")
        void nuevoEsBorrador() {
            assertThat(new Costeo().getEstado()).isEqualTo(EstadoCosteo.BORRADOR);
        }

        @Test
        @DisplayName("flujo feliz BORRADOR → COSTEADO → APROBADO")
        void flujoFeliz() {
            Costeo c = new Costeo();
            c.marcarCosteado();
            assertThat(c.getEstado()).isEqualTo(EstadoCosteo.COSTEADO);
            c.aprobar();
            assertThat(c.getEstado()).isEqualTo(EstadoCosteo.APROBADO);
        }

        @Test
        @DisplayName("aprobar un BORRADOR es una transición inválida")
        void aprobarBorradorFalla() {
            assertThatThrownBy(() -> new Costeo().aprobar())
                    .isInstanceOf(BusinessRuleException.class);
        }

        @Test
        @DisplayName("APROBADO es terminal: no se puede reabrir ni rechazar")
        void aprobadoEsTerminal() {
            Costeo c = new Costeo();
            c.marcarCosteado();
            c.aprobar();
            assertThatThrownBy(c::reabrir).isInstanceOf(BusinessRuleException.class);
            assertThatThrownBy(() -> c.rechazar("x")).isInstanceOf(BusinessRuleException.class);
        }

        @Test
        @DisplayName("rechazar exige motivo y lo conserva; luego se puede reabrir a BORRADOR")
        void rechazoConMotivoYReproceso() {
            Costeo c = new Costeo();
            c.marcarCosteado();

            assertThatThrownBy(() -> c.rechazar("  ")).isInstanceOf(BusinessRuleException.class);

            c.rechazar("Diferencias en costo de tela");
            assertThat(c.getEstado()).isEqualTo(EstadoCosteo.RECHAZADO);
            assertThat(c.getMotivoRechazo()).isEqualTo("Diferencias en costo de tela");

            c.reabrir();
            assertThat(c.getEstado()).isEqualTo(EstadoCosteo.BORRADOR);
            assertThat(c.getMotivoRechazo()).isNull();
        }
    }
}
