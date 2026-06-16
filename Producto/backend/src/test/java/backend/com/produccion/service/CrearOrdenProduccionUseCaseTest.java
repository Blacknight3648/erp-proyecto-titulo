package backend.com.produccion.service;

import backend.com.comercial.domain.enums.EstadoEVN;
import backend.com.comercial.domain.enums.EstadoNV;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.ItemEVN;
import backend.com.comercial.domain.model.ItemNV;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.produccion.application.UseCase.CrearOrdenProduccionUseCase;
import backend.com.produccion.application.UseCase.CrearVersionCosteoUseCase;
import backend.com.produccion.domain.enums.FaseProduccion;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.CosteoVersion;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.model.OrdenProduccionItem;
import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import backend.com.shared.exception.EntityNotFoundException;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests del UseCase con dependencias mockeadas (Mockito).
 * Cubre UT-PRD-004 (parcial): generación automática de la OP y de las OTs por
 * fase a partir de una Nota de Venta, incluyendo la herencia del número desde
 * el Costeo vinculado y la decisión de fases según la información de logo.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CrearOrdenProduccionUseCase")
class CrearOrdenProduccionUseCaseTest {

    @Mock
    private OrdenProduccionRepository repository;
    @Mock
    private EvaluacionNegocioRepository evnRepository;
    @Mock
    private CosteoRepository costeoRepository;
    @Mock
    private OrdenTrabajoRepository otRepository;
    @Mock
    private CrearVersionCosteoUseCase crearVersionCosteoUseCase;
    @Mock
    private backend.com.shared.application.service.NumeroDocumentoService numeroDocumentoService;

    @InjectMocks
    private CrearOrdenProduccionUseCase useCase;

    @BeforeEach
    void setUp() {
        org.mockito.Mockito.lenient().when(repository.save(any(OrdenProduccion.class))).thenAnswer(inv -> inv.getArgument(0));
        // La OP genera su propio correlativo independiente vía NumeroDocumentoService.
        org.mockito.Mockito.lenient().when(numeroDocumentoService.siguienteFormateado("OP"))
                .thenReturn(new DocumentNumber("OP-0000001"));
    }

    private NotaVenta notaVenta(Long evaluacionNegocioId, List<ItemNV> items) {
        return new NotaVenta(1L, new DocumentNumber("NV-001"), evaluacionNegocioId, 10L, 20L,
                EstadoNV.APROBADA, false, null, LocalDate.now(), LocalDate.now().plusDays(10),
                Money.zero("CLP"), Money.zero("CLP"), Money.zero("CLP"), items);
    }

    private ItemNV itemOP(int nroItem, Integer cantidad, String llevaLogo, String logoDetalle) {
        return new ItemNV((long) (100 + nroItem), nroItem, 1, "Polera Basica", "Algodón", "100% Algodón",
                "Azul", "M", "Unisex", "COD-" + nroItem, 5L, llevaLogo, "OP", true, "Detalle OT",
                logoDetalle, cantidad, new Money(new BigDecimal("5000"), "CLP"), List.of());
    }

    private ItemNV itemNoOP(int nroItem) {
        return new ItemNV((long) (200 + nroItem), nroItem, 2, "Tela Insumo", null, null, null, null, null,
                "COD-X" + nroItem, null, "N/A", "INSUMO", false, null, null, 10,
                new Money(new BigDecimal("1000"), "CLP"), List.of());
    }

    private ItemEVN itemEvnConCosteo(Integer nroItem, Long costeoId) {
        return new ItemEVN(1, 5L, nroItem, "Polera", "Polera Basica", "Algodón", "100% Algodón", "Unisex",
                "COD-INT", "COD-PROV", "Proveedor", 50, new Money(new BigDecimal("5000"), "CLP"),
                new Money(new BigDecimal("3000"), "CLP"), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                "OP", List.of(), costeoId, 30L);
    }

    private ItemEVN itemEvnSinCosteo(Integer nroItem) {
        return new ItemEVN(1, 5L, nroItem, "Polera", "Polera Basica", "Algodón", "100% Algodón", "Unisex",
                "COD-INT", "COD-PROV", "Proveedor", 50, new Money(new BigDecimal("5000"), "CLP"),
                new Money(new BigDecimal("3000"), "CLP"), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                "OP", List.of(), null, 30L);
    }

    private EvaluacionNegocio evn(Long evnId, List<ItemEVN> items) {
        return new EvaluacionNegocio(evnId, new DocumentNumber("EVN-001"), 10L, 20L,
                EstadoEVN.APROBADA, LocalDate.now(), null, List.of(), items, BigDecimal.ZERO,
                "Cliente Test", "REF-001", "Vendedor Test");
    }

    private Costeo costeo(Long idCosteo, String numeroCosteo) {
        return new Costeo(idCosteo, numeroCosteo != null ? new DocumentNumber(numeroCosteo) : null,
                30L, 10L, "Cliente Test", 20L, "Vendedor Test",
                Money.zero("CLP"), Money.zero("CLP"), Money.zero("CLP"), Money.zero("CLP"), Money.zero("CLP"),
                BigDecimal.ZERO, null, null, null, null, null, null,
                Money.zero("CLP"), BigDecimal.ZERO, Money.zero("CLP"), new ArrayList<>());
    }

    private CosteoVersion costeoVersion(Long id, Long costeoId, int numeroVersion) {
        return new CosteoVersion(id, costeoId, numeroVersion, LocalDateTime.now(), "Versión inicial al crear OP",
                "SYSTEM", new ArrayList<>(), Money.zero("CLP"), Money.zero("CLP"), Money.zero("CLP"),
                Money.zero("CLP"), Money.zero("CLP"), BigDecimal.ZERO, Money.zero("CLP"), BigDecimal.ZERO,
                Money.zero("CLP"));
    }

    @Test
    @DisplayName("falla si la Nota de Venta es nula")
    void execute_conNotaVentaNula_lanzaValidationException() {
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Nota de Venta");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("sin Evaluación de Negocio crea la OP con su propio correlativo y genera las OTs base")
    void execute_sinEvaluacionNegocio_creaOPConNumeroPropioYGeneraTresFases() {
        ItemNV item = itemOP(1, 10, "N/A", null);
        NotaVenta nv = notaVenta(null, List.of(item));

        OrdenProduccion result = useCase.execute(nv);

        assertThat(result.getNumeroOP().getValue()).isEqualTo("OP-0000001");
        assertThat(result.getCosteoVersionId()).isNull();
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getCantidad()).isEqualTo(10);

        verify(evnRepository, never()).findById(any());
        verify(repository).save(any(OrdenProduccion.class));
        verify(otRepository, times(3)).save(any(OrdenTrabajo.class));
    }

    @Test
    @DisplayName("falla con EntityNotFoundException si la Evaluación de Negocio vinculada no existe")
    void execute_conEvaluacionNegocioInexistente_lanzaEntityNotFoundException() {
        NotaVenta nv = notaVenta(5L, List.of());
        when(evnRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(nv))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Evaluación de Negocio no encontrada: 5");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("si la EVN no tiene un ítem OP con costeo vinculado, la OP usa su propio correlativo")
    void execute_conEvnSinItemDeCosteoVinculado_usaNumeroPropio() {
        ItemNV item = itemOP(1, 5, "N/A", null);
        NotaVenta nv = notaVenta(7L, List.of(item));
        EvaluacionNegocio evnEntity = evn(7L, List.of(itemEvnSinCosteo(1)));

        when(evnRepository.findById(7L)).thenReturn(Optional.of(evnEntity));

        OrdenProduccion result = useCase.execute(nv);

        assertThat(result.getNumeroOP().getValue()).isEqualTo("OP-0000001");
        assertThat(result.getCosteoVersionId()).isNull();
        verify(costeoRepository, never()).findById(any());
        verify(crearVersionCosteoUseCase, never()).ejecutar(any(), any(), any());
    }

    @Test
    @DisplayName("falla con EntityNotFoundException si el Costeo vinculado al ítem OP de la EVN no existe")
    void execute_conCosteoVinculadoInexistente_lanzaEntityNotFoundException() {
        ItemNV item = itemOP(1, 5, "N/A", null);
        NotaVenta nv = notaVenta(7L, List.of(item));
        EvaluacionNegocio evnEntity = evn(7L, List.of(itemEvnConCosteo(1, 50L)));

        when(evnRepository.findById(7L)).thenReturn(Optional.of(evnEntity));
        when(costeoRepository.findById(50L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(nv))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Costeo no encontrado: 50");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("con Costeo vinculado, la OP usa su propio correlativo (no hereda) y vincula la nueva versión")
    void execute_conCosteoVinculado_usaNumeroPropioYVinculaVersion() {
        ItemNV item = itemOP(1, 5, "N/A", null);
        NotaVenta nv = notaVenta(7L, List.of(item));
        EvaluacionNegocio evnEntity = evn(7L, List.of(itemEvnConCosteo(1, 50L)));
        Costeo costeoEntity = costeo(50L, "COSTEO-050");
        CosteoVersion version = costeoVersion(777L, 50L, 1);

        when(evnRepository.findById(7L)).thenReturn(Optional.of(evnEntity));
        when(costeoRepository.findById(50L)).thenReturn(Optional.of(costeoEntity));
        when(crearVersionCosteoUseCase.ejecutar(50L, "Versión inicial al crear OP", "SYSTEM"))
                .thenReturn(version);

        OrdenProduccion result = useCase.execute(nv);

        assertThat(result.getNumeroOP().getValue()).isEqualTo("OP-0000001");
        assertThat(result.getCosteoVersionId()).isEqualTo(777L);
        verify(crearVersionCosteoUseCase).ejecutar(50L, "Versión inicial al crear OP", "SYSTEM");
    }

    @Test
    @DisplayName("si el ítem lleva logo de bordado, genera 4 fases incluyendo BORDADO")
    void execute_conLogoBordado_generaCuatroFasesIncluyendoBordado() {
        ItemNV item = itemOP(1, 20, "Sí", "Logo bordado en pecho");
        NotaVenta nv = notaVenta(null, List.of(item));

        useCase.execute(nv);

        ArgumentCaptor<OrdenTrabajo> captor = ArgumentCaptor.forClass(OrdenTrabajo.class);
        verify(otRepository, times(4)).save(captor.capture());

        assertThat(captor.getAllValues()).extracting(OrdenTrabajo::getFase)
                .containsExactly(FaseProduccion.CORTE, FaseProduccion.CONFECCION,
                        FaseProduccion.BORDADO, FaseProduccion.TERMINACION);
        assertThat(captor.getAllValues())
                .allSatisfy(ot -> assertThat(ot.getCantidadTotal()).isEqualTo(20));
    }

    @Test
    @DisplayName("si la información de logo menciona estampado, genera 4 fases incluyendo ESTAMPADO")
    void execute_conLogoEstampado_generaCuatroFasesIncluyendoEstampado() {
        ItemNV item = itemOP(1, 15, "Sí", "Estampado full color en espalda");
        NotaVenta nv = notaVenta(null, List.of(item));

        useCase.execute(nv);

        ArgumentCaptor<OrdenTrabajo> captor = ArgumentCaptor.forClass(OrdenTrabajo.class);
        verify(otRepository, times(4)).save(captor.capture());

        assertThat(captor.getAllValues()).extracting(OrdenTrabajo::getFase)
                .containsExactly(FaseProduccion.CORTE, FaseProduccion.CONFECCION,
                        FaseProduccion.ESTAMPADO, FaseProduccion.TERMINACION);
    }

    @Test
    @DisplayName("sin logo, genera 3 fases (registros OT) para el ítem")
    void execute_sinLogo_generaTresFases() {
        ItemNV item = itemOP(2, 8, "NO", null);
        NotaVenta nv = notaVenta(null, List.of(item));

        OrdenProduccion result = useCase.execute(nv);

        ArgumentCaptor<OrdenTrabajo> captor = ArgumentCaptor.forClass(OrdenTrabajo.class);
        verify(otRepository, times(3)).save(captor.capture());

        List<OrdenTrabajo> ots = captor.getAllValues();
        assertThat(ots).extracting(OrdenTrabajo::getFase)
                .containsExactly(FaseProduccion.CORTE, FaseProduccion.CONFECCION, FaseProduccion.TERMINACION);
        // La OT es un registro sin número propio: se asocia al ítem por su nroItem.
        assertThat(ots).allSatisfy(ot -> {
            assertThat(ot.getNroItem()).isEqualTo(2);
            assertThat(ot.getCantidadTotal()).isEqualTo(8);
            assertThat(ot.getNotaVentaId()).isEqualTo(result.getNotaVentaId());
            assertThat(ot.getOrdenProduccionId()).isNull();
        });
        assertThat(ots.get(0).getObservaciones()).isEqualTo("Corte - Polera Basica");
    }

    @Test
    @DisplayName("ítems que no son de tipo OP no se agregan a la OP ni generan OTs")
    void execute_itemsNoOP_noGeneranOTsNiSeAgreganALaOP() {
        ItemNV opItem = itemOP(1, 10, "N/A", null);
        ItemNV insumoItem = itemNoOP(2);
        NotaVenta nv = notaVenta(null, List.of(opItem, insumoItem));

        OrdenProduccion result = useCase.execute(nv);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getNroItem()).isEqualTo(1);
        verify(otRepository, times(3)).save(any(OrdenTrabajo.class));
    }

    @Test
    @DisplayName("mapea correctamente los campos de ItemNV al ítem de la Orden de Producción")
    void execute_mapeaCamposDeItemNVAItemOP() {
        ItemNV item = itemOP(3, 25, "N/A", null);
        NotaVenta nv = notaVenta(null, List.of(item));

        OrdenProduccion result = useCase.execute(nv);

        OrdenProduccionItem opItem = result.getItems().get(0);
        assertThat(opItem.getArticuloId()).isEqualTo(item.getArticuloId());
        assertThat(opItem.getNroItem()).isEqualTo(item.getNroItem());
        assertThat(opItem.getModelo()).isEqualTo(item.getModelo());
        assertThat(opItem.getTela()).isEqualTo(item.getTela());
        assertThat(opItem.getComposicion()).isEqualTo(item.getComposicion());
        assertThat(opItem.getColor()).isEqualTo(item.getColor());
        assertThat(opItem.getTalla()).isEqualTo(item.getTalla());
        assertThat(opItem.getGenero()).isEqualTo(item.getGenero());
        assertThat(opItem.getCodigo()).isEqualTo(item.getCodigo());
        assertThat(opItem.getCantidad()).isEqualTo(25);
    }
}
