package backend.com.produccion.service;

import backend.com.produccion.application.UseCase.CrearVersionCosteoUseCase;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.CosteoItem;
import backend.com.produccion.domain.model.CosteoVersion;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.domain.repository.CosteoVersionRepository;
import backend.com.shared.exception.ValidationException;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests del UseCase con dependencias mockeadas (Mockito).
 * Cubre UT-PRD-002 (versionado del Costeo): cada llamada a ejecutar() debe
 * generar un snapshot inmutable de los ítems y costos vigentes, incrementando
 * el número de versión, sin alterar versiones previamente creadas.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CrearVersionCosteoUseCase")
class CrearVersionCosteoUseCaseTest {

    @Mock
    private CosteoRepository costeoRepository;
    @Mock
    private CosteoVersionRepository costeoVersionRepository;

    @InjectMocks
    private CrearVersionCosteoUseCase useCase;

    private Costeo costeo;
    private CosteoItem itemTela;

    @BeforeEach
    void setUp() {
        itemTela = CosteoItem.builder()
                .idCosteoItem(1L)
                .tipoInsumo("TELAS")
                .articuloId(100)
                .nombreInsumo("Tela Algodón")
                .consumo(new BigDecimal("10"))
                .precioUnitario(new BigDecimal("1000"))
                .costoTotal(new BigDecimal("10000"))
                .build();

        costeo = new Costeo(
                5L,
                new DocumentNumber("COSTEO-001"),
                20L,
                1L, "Cliente Test", 2L, "Vendedor Test",
                new Money(new BigDecimal("5000"), "CLP"), // costoHilos
                new Money(new BigDecimal("20000"), "CLP"), // costoManoObra
                "", // observacionesManoObra
                new Money(new BigDecimal("1000"), "CLP"), // costoEtiquetas
                new Money(new BigDecimal("2000"), "CLP"), // costoEmbalaje
                new Money(new BigDecimal("3000"), "CLP"), // costoFlete
                new BigDecimal("10"), // porcentajeCostoFijo
                null, null, null, null, null, null, // cintas/vivo (no usados en el versionado)
                new Money(new BigDecimal("10000"), "CLP"), // costoTotalMateriaPrima
                new BigDecimal("30"), // margenBrutoSugerido
                new Money(new BigDecimal("50000"), "CLP"), // precioVentaSugerido
                new ArrayList<>(List.of(itemTela)));

        org.mockito.Mockito.lenient().when(costeoVersionRepository.save(any(CosteoVersion.class)))
                .thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    @DisplayName("falla si el costeoId es nulo")
    void ejecutar_conCosteoIdNulo_lanzaValidationException() {
        assertThatThrownBy(() -> useCase.ejecutar(null, "motivo", "user1"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("costeoId");

        verify(costeoVersionRepository, never()).save(any());
    }

    @Test
    @DisplayName("falla si el Costeo no existe")
    void ejecutar_conCosteoInexistente_lanzaValidationException() {
        when(costeoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.ejecutar(99L, "motivo", "user1"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("No existe el Costeo con id: 99");

        verify(costeoVersionRepository, never()).save(any());
    }

    @Test
    @DisplayName("crea la primera versión (V1) con snapshot de ítems y costos vigentes")
    void ejecutar_creaPrimeraVersionConSnapshotDeItemsYCostos() {
        when(costeoRepository.findById(5L)).thenReturn(Optional.of(costeo));
        when(costeoVersionRepository.siguienteNumeroVersion(5L)).thenReturn(1);

        CosteoVersion v1 = useCase.ejecutar(5L, "Versión inicial al crear OP", "SYSTEM");

        assertThat(v1.getCosteoId()).isEqualTo(5L);
        assertThat(v1.getNumeroVersion()).isEqualTo(1);
        assertThat(v1.getMotivoCambio()).isEqualTo("Versión inicial al crear OP");
        assertThat(v1.getUsuarioCreador()).isEqualTo("SYSTEM");

        assertThat(v1.getItems()).hasSize(1);
        assertThat(v1.getItems().get(0).getCosteoItemId()).isEqualTo(1L);
        assertThat(v1.getItems().get(0).getNombreInsumo()).isEqualTo("Tela Algodón");
        assertThat(v1.getItems().get(0).getConsumo()).isEqualByComparingTo(new BigDecimal("10"));
        assertThat(v1.getItems().get(0).getCostoTotal()).isEqualByComparingTo(new BigDecimal("10000"));

        // Los costos generales del Costeo quedan congelados en la versión
        assertThat(v1.getTotalManoObra()).isEqualTo(costeo.getCostoManoObra());
        assertThat(v1.getTotalHilo()).isEqualTo(costeo.getCostoHilos());
        assertThat(v1.getCostoTotalMateriaPrima()).isEqualTo(costeo.getCostoTotalMateriaPrima());

        verify(costeoVersionRepository).save(any(CosteoVersion.class));
    }

    @Test
    @DisplayName("la segunda versión (V2) incrementa el número de versión")
    void ejecutar_segundaVersion_incrementaNumeroDeVersion() {
        when(costeoRepository.findById(5L)).thenReturn(Optional.of(costeo));
        when(costeoVersionRepository.siguienteNumeroVersion(5L)).thenReturn(2);

        CosteoVersion v2 = useCase.ejecutar(5L, "Ajuste de consumo de tela", "usuario1");

        assertThat(v2.getNumeroVersion()).isEqualTo(2);
        assertThat(v2.getMotivoCambio()).isEqualTo("Ajuste de consumo de tela");
    }

    @Test
    @DisplayName("modificar el Costeo original después de crear V1 no altera el snapshot de V1 (inmutabilidad)")
    void ejecutar_modificarCosteoOriginal_noAfectaSnapshotDeVersionAnterior() {
        when(costeoRepository.findById(5L)).thenReturn(Optional.of(costeo));
        when(costeoVersionRepository.siguienteNumeroVersion(5L)).thenReturn(1, 2);

        ArgumentCaptor<CosteoVersion> captor = ArgumentCaptor.forClass(CosteoVersion.class);

        // V1: snapshot con consumo = 10
        CosteoVersion v1 = useCase.ejecutar(5L, "Versión inicial", "SYSTEM");

        // Se modifica el Costeo "vivo" (p.ej. el usuario corrige el consumo de tela)
        itemTela.setConsumo(new BigDecimal("99"));

        // V2: snapshot con consumo = 99
        CosteoVersion v2 = useCase.ejecutar(5L, "Corrección de consumo", "usuario1");

        verify(costeoVersionRepository, org.mockito.Mockito.times(2)).save(captor.capture());
        List<CosteoVersion> guardadas = captor.getAllValues();
        assertThat(guardadas).hasSize(2);

        // V1 sigue íntegra: su snapshot conserva el consumo original (10)
        assertThat(v1.getNumeroVersion()).isEqualTo(1);
        assertThat(v1.getItems().get(0).getConsumo()).isEqualByComparingTo(new BigDecimal("10"));

        // V2 refleja el nuevo consumo (99), V1 no fue afectada
        assertThat(v2.getNumeroVersion()).isEqualTo(2);
        assertThat(v2.getItems().get(0).getConsumo()).isEqualByComparingTo(new BigDecimal("99"));
        assertThat(v1.getItems().get(0).getConsumo()).isEqualByComparingTo(new BigDecimal("10"));
    }

    @Test
    @DisplayName("la lista de ítems de una CosteoVersion es inmutable")
    void getItems_deCosteoVersion_esInmutable() {
        when(costeoRepository.findById(5L)).thenReturn(Optional.of(costeo));
        when(costeoVersionRepository.siguienteNumeroVersion(5L)).thenReturn(1);

        CosteoVersion v1 = useCase.ejecutar(5L, "Versión inicial", "SYSTEM");
        List<backend.com.produccion.domain.model.CosteoItemVersion> items = v1.getItems();

        assertThatThrownBy(() -> items.add(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("usa siguienteNumeroVersion del repositorio para numerar la nueva versión")
    void ejecutar_consultaSiguienteNumeroVersionDelCosteo() {
        when(costeoRepository.findById(5L)).thenReturn(Optional.of(costeo));
        when(costeoVersionRepository.siguienteNumeroVersion(5L)).thenReturn(7);

        CosteoVersion version = useCase.ejecutar(5L, "motivo", "user1");

        assertThat(version.getNumeroVersion()).isEqualTo(7);
        verify(costeoVersionRepository).siguienteNumeroVersion(5L);
        verify(costeoVersionRepository, never()).siguienteNumeroVersion(org.mockito.ArgumentMatchers.argThat(id -> !id.equals(5L)));
    }
}
