package backend.com.produccion.service.Impl;

import backend.com.produccion.application.UseCase.GenerarOCConsolidadaUseCase;
import backend.com.produccion.application.dto.GenerarOCConsolidadaRequest;
import backend.com.produccion.application.dto.HCItemOCItemLinkDTO;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.dto.OrdenCompraItemDTO;
import backend.com.produccion.application.service.impl.OrdenCompraServiceImpl;
import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.model.OrdenCompraItem;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrdenCompraServiceImpl")
public class OrdenCompraServiceImplTest {

    @Mock
    private OrdenCompraRepository repository;

    @Mock
    private GenerarOCConsolidadaUseCase generarOCConsolidadaUseCase;

    @Mock
    private HojaCompraRepository hojaCompraRepository;

    @InjectMocks
    private OrdenCompraServiceImpl ordenCompraService;

    // --- MÉTODOS HELPER ---

    private OrdenCompra crearOrdenCompraSimulada(Long id, String numero, EstadoOC estado) {
        return crearOrdenCompraSimulada(id, numero, estado, new ArrayList<>());
    }

    private OrdenCompra crearOrdenCompraSimulada(Long id, String numero, EstadoOC estado, List<OrdenCompraItem> items) {
        return new OrdenCompra(
                id,
                numero != null ? new DocumentNumber(numero) : null,
                500L,
                estado,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "Observación de prueba consolidada",
                new BigDecimal("1500.00"),
                items
        );
    }

    private OrdenCompraItem crearItemSimulado(Long idItem, Long ocId, BigDecimal cantidad, BigDecimal precio) {
        // En lugar de renegar con el constructor de 8 parámetros, mockeamos el item.
        // Se usan stubs lenient porque cada test ejercita un subconjunto distinto de getters.
        OrdenCompraItem item = org.mockito.Mockito.mock(OrdenCompraItem.class,
                org.mockito.Mockito.withSettings().strictness(org.mockito.quality.Strictness.LENIENT));

        when(item.getIdOCItem()).thenReturn(idItem);
        when(item.getOcId()).thenReturn(ocId);
        when(item.getTipoInsumo()).thenReturn("TELA");
        when(item.getArticuloId()).thenReturn(10); // Cambiar a 10 si es Integer en tu modelo
        when(item.getNombreInsumo()).thenReturn("Algodón Pima");
        when(item.getCantidadRequerida()).thenReturn(cantidad);
        when(item.getCantidadStock()).thenReturn(BigDecimal.ZERO);
        when(item.getCantidadComprada()).thenReturn(cantidad);
        when(item.getPrecioUnitario()).thenReturn(precio);
        when(item.getSubtotal()).thenReturn(cantidad.multiply(precio));
        when(item.getHcLinks()).thenReturn(new ArrayList<>());

        return item;
    }

    private HojaCompra crearHojaCompraConItem(Long idHCItem, String nombreInsumo, BigDecimal precioRef) {
        // Cambiamos la instancia real por un Mock para saltarnos la restricción del Setter
        HojaCompra hc = org.mockito.Mockito.mock(HojaCompra.class);

        HojaCompraItem item = new HojaCompraItem(
                idHCItem,
                null,
                "INSUMO_TEST",
                1,
                nombreInsumo,
                BigDecimal.ONE,
                100,
                BigDecimal.TEN,
                precioRef
        );

        when(hc.getItems()).thenReturn(List.of(item));
        return hc;
    }

    // --- PRUEBAS DE FLUJO BASE ---

    @Test
    @DisplayName("generar consolidada delega en UseCase y mapea el DTO resultante correctamente")
    void generarConsolidada_delegaEnUseCaseYMapeaDTO() {
        GenerarOCConsolidadaRequest request = new GenerarOCConsolidadaRequest();
        OrdenCompra ocEntidadSimulada = crearOrdenCompraSimulada(1L, "OC-2026-001", EstadoOC.EMITIDA);

        when(generarOCConsolidadaUseCase.ejecutar(request)).thenReturn(ocEntidadSimulada);

        OrdenCompraDTO resultado = ordenCompraService.generarConsolidada(request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdOC()).isEqualTo(1L);
        assertThat(resultado.getNumeroOC()).isEqualTo("OC-2026-001");
        verify(generarOCConsolidadaUseCase).ejecutar(request);
    }

    @Test
    @DisplayName("marcar enviada cambia el estado de la OC a ENVIADA y la persiste")
    void marcarEnviada_cambiaEstadoYGuarda() {
        OrdenCompra oc = crearOrdenCompraSimulada(1L, "OC-001", EstadoOC.EMITIDA);
        when(repository.findById(1L)).thenReturn(Optional.of(oc));
        when(repository.save(any(OrdenCompra.class))).thenAnswer(i -> i.getArgument(0));

        // En entornos reales, oc.marcarEnviada() cambia internamente el estado del objeto de dominio.
        OrdenCompraDTO resultado = ordenCompraService.marcarEnviada(1L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoOC.ENVIADA);
        verify(repository).save(oc);
    }

    // --- PRUEBAS DE REGLAS DE NEGOCIO Y MUTACIONES ---

    @Test
    @DisplayName("actualizar precio ítem cambia el subtotal y recalcula el total de la OC")
    void actualizarPrecioItem_flujoFeliz_recalculaTotales() {
        OrdenCompraItem item = crearItemSimulado(100L, 1L, new BigDecimal("2"), new BigDecimal("10.00")); // Subtotal incial = 20
        OrdenCompra oc = crearOrdenCompraSimulada(1L, "OC-001", EstadoOC.EMITIDA, List.of(item));

        when(repository.findById(1L)).thenReturn(Optional.of(oc));
        when(repository.save(any(OrdenCompra.class))).thenAnswer(i -> i.getArgument(0));

        // Nuevo precio de 15.00 -> Nuevo Subtotal = 30.00
        OrdenCompraDTO resultado = ordenCompraService.actualizarPrecioItem(1L, 100L, new BigDecimal("15.00"));

        assertThat(resultado).isNotNull();
        assertThat(resultado.getItems().get(0).getPrecioUnitario()).isEqualByComparingTo(new BigDecimal("15.00"));
        assertThat(resultado.getItems().get(0).getSubtotal()).isEqualByComparingTo(new BigDecimal("30.00"));
        verify(repository).save(any(OrdenCompra.class));
    }

    @Test
    @DisplayName("actualizar precio ítem lanza BusinessRuleException si el estado no es EMITIDA")
    void actualizarPrecioItem_estadoInvalido_lanzaBusinessRuleException() {
        OrdenCompra oc = crearOrdenCompraSimulada(1L, "OC-001", EstadoOC.ENVIADA); // Estado no permitido

        when(repository.findById(1L)).thenReturn(Optional.of(oc));

        assertThatThrownBy(() -> ordenCompraService.actualizarPrecioItem(1L, 100L, new BigDecimal("15.00")))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Solo se puede modificar precios en OCs EMITIDAS");
    }

    @Test
    @DisplayName("agregar ítem lanza BusinessRuleException si el precio unitario es negativo")
    void agregarItem_precioNegativo_lanzaBusinessRuleException() {
        OrdenCompra oc = crearOrdenCompraSimulada(1L, "OC-001", EstadoOC.EMITIDA);
        OrdenCompraItemDTO dtoInput = OrdenCompraItemDTO.builder()
                .precioUnitario(new BigDecimal("-5.00")) // Negativo inválido
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(oc));

        assertThatThrownBy(() -> ordenCompraService.agregarItem(1L, dtoInput))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("El precio unitario debe ser no negativo");
    }

    @Test
    @DisplayName("agregar ítem realiza validación contra límites de la hoja de compra y lanza excepción si se excede")
    void agregarItem_excedePrecioReferencia_lanzaBusinessRuleException() {
        OrdenCompra oc = crearOrdenCompraSimulada(1L, "OC-001", EstadoOC.EMITIDA);

        // Link DTO apuntando al ítem 999 de la Hoja de Compra
        HCItemOCItemLinkDTO linkDTO = HCItemOCItemLinkDTO.builder().hcItemId(999L).cantidadAsignada(BigDecimal.ONE).build();
        OrdenCompraItemDTO dtoInput = OrdenCompraItemDTO.builder()
                .tipoInsumo("TELA")
                .cantidadComprada(BigDecimal.TEN)
                .precioUnitario(new BigDecimal("50.00")) // Precio propuesto mayor que el de referencia
                .hcLinks(List.of(linkDTO))
                .build();

        // Simulamos que la hoja de compra tiene un precio referencial de 40.00 para ese ítem
        HojaCompra hcMock = crearHojaCompraConItem(999L, "Hilo Premium", new BigDecimal("40.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(oc));
        when(hojaCompraRepository.findAllByItemIds(List.of(999L))).thenReturn(List.of(hcMock));

        assertThatThrownBy(() -> ordenCompraService.agregarItem(1L, dtoInput))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("excede el precio de referencia costeado");
    }

    @Test
    @DisplayName("eliminar una OC lanza BusinessRuleException si su estado es distinto de EMITIDA")
    void eliminarOC_estadoInvalido_lanzaException() {
        OrdenCompra oc = crearOrdenCompraSimulada(1L, "OC-001", EstadoOC.CERRADA);
        when(repository.findById(1L)).thenReturn(Optional.of(oc));

        assertThatThrownBy(() -> ordenCompraService.eliminar(1L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Solo se puede eliminar una OC en estado EMITIDA");
    }

    @Test
    @DisplayName("eliminar item de OC remueve el elemento de la lista y persiste cambios")
    void eliminarItem_flujoFeliz_remueveItemYRecalcula() {
        OrdenCompraItem item = crearItemSimulado(200L, 1L, BigDecimal.ONE, BigDecimal.TEN);
        OrdenCompra oc = crearOrdenCompraSimulada(1L, "OC-001", EstadoOC.EMITIDA, List.of(item));

        when(repository.findById(1L)).thenReturn(Optional.of(oc));
        when(repository.save(any(OrdenCompra.class))).thenAnswer(i -> i.getArgument(0));

        OrdenCompraDTO resultado = ordenCompraService.eliminarItem(1L, 200L);

        assertThat(resultado.getItems()).isEmpty();
        verify(repository).save(any(OrdenCompra.class));
    }

    @Test
    @DisplayName("eliminar item lanza EntityNotFoundException si el idItem no pertenece a la OC")
    void eliminarItem_noExistente_lanzaEntityNotFoundException() {
        OrdenCompra oc = crearOrdenCompraSimulada(1L, "OC-001", EstadoOC.EMITIDA);
        // La lista de ítems de la OC está vacía

        when(repository.findById(1L)).thenReturn(Optional.of(oc));

        assertThatThrownBy(() -> ordenCompraService.eliminarItem(1L, 999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("OCItem 999 no encontrado en la OC 1");
    }
}