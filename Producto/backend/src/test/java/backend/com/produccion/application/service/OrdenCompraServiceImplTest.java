package backend.com.produccion.application.service;

import backend.com.produccion.application.UseCase.GenerarOCConsolidadaUseCase;
import backend.com.produccion.application.dto.HCItemOCItemLinkDTO;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.dto.OrdenCompraItemDTO;
import backend.com.produccion.application.service.impl.OrdenCompraServiceImpl;
import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.produccion.domain.model.*;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrdenCompraServiceImpl")
class OrdenCompraServiceImplTest {

        @Mock
        private OrdenCompraRepository ordenCompraRepository;
        @Mock
        private GenerarOCConsolidadaUseCase generarOCConsolidadaUseCase;
        @Mock
        private HojaCompraRepository hojaCompraRepository;

        @InjectMocks
        private OrdenCompraServiceImpl service;

        private OrdenCompra ocEmitida;
        private OrdenCompra ocEnviada;

        @BeforeEach
        void setUp() {
                List<OrdenCompraItem> items = new ArrayList<>();
                items.add(new OrdenCompraItem(10L, 1L, "INSUMO", 100, "Insumo Test",
                                BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN,
                                BigDecimal.valueOf(15.0), BigDecimal.valueOf(150.0), new ArrayList<>()));

                ocEmitida = new OrdenCompra(1L, new DocumentNumber("OC-001"), 2L, EstadoOC.EMITIDA,
                                LocalDate.now(), LocalDate.now().plusDays(5), "Obs",
                                BigDecimal.valueOf(150.0), items);

                ocEnviada = new OrdenCompra(2L, new DocumentNumber("OC-002"), 2L, EstadoOC.ENVIADA,
                                LocalDate.now(), LocalDate.now().plusDays(5), "Obs",
                                BigDecimal.valueOf(150.0), items);
        }

        @Test
        @DisplayName("eliminar OC exitoso en estado EMITIDA")
        void eliminarExitoso() {
                when(ordenCompraRepository.findById(1L)).thenReturn(Optional.of(ocEmitida));

                service.eliminar(1L);

                verify(ordenCompraRepository).deleteById(1L);
        }

        @Test
        @DisplayName("eliminar OC lanza excepcion si estado no es EMITIDA")
        void eliminarFallaPorEstado() {
                when(ordenCompraRepository.findById(2L)).thenReturn(Optional.of(ocEnviada));

                assertThatThrownBy(() -> service.eliminar(2L))
                                .isInstanceOf(BusinessRuleException.class)
                                .hasMessageContaining("Solo se puede eliminar una OC en estado EMITIDA");

                verify(ordenCompraRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("agregar item manual exitosamente (sin hcLinks)")
        void agregarItemManualExitoso() {
                when(ordenCompraRepository.findById(1L)).thenReturn(Optional.of(ocEmitida));
                when(ordenCompraRepository.save(any(OrdenCompra.class))).thenAnswer(inv -> inv.getArgument(0));

                OrdenCompraItemDTO itemDTO = OrdenCompraItemDTO.builder()
                                .tipoInsumo("INSUMO")
                                .articuloId(101)
                                .nombreInsumo("Insumo Extra")
                                .cantidadComprada(BigDecimal.valueOf(2.0))
                                .precioUnitario(BigDecimal.valueOf(20.0))
                                .build();

                OrdenCompraDTO res = service.agregarItem(1L, itemDTO);

                assertThat(res.getItems()).hasSize(2);
                assertThat(res.getTotalNeto()).isEqualByComparingTo(BigDecimal.valueOf(190.0));
                verify(ordenCompraRepository).save(any(OrdenCompra.class));
        }

        @Test
        @DisplayName("agregar item con hcLink valida limite de precio y falla si se excede")
        void agregarItemHcLinkExcedePrecio() {
                when(ordenCompraRepository.findById(1L)).thenReturn(Optional.of(ocEmitida));

                HojaCompraItem hcItem = new HojaCompraItem(20L, 5L, "INSUMO", 101, "Insumo Extra",
                                BigDecimal.ONE, 10, BigDecimal.TEN, BigDecimal.valueOf(18.0), 2L, "PROV", null, null);
                HojaCompra hc = new HojaCompra(5L, new DocumentNumber("HC-01"), 1L, 10L, EstadoHC.APROBADA,
                                LocalDate.now(), null, List.of(hcItem));
                when(hojaCompraRepository.findAllByItemIds(List.of(20L))).thenReturn(List.of(hc));

                OrdenCompraItemDTO itemDTO = OrdenCompraItemDTO.builder()
                                .tipoInsumo("INSUMO")
                                .articuloId(101)
                                .nombreInsumo("Insumo Extra")
                                .cantidadComprada(BigDecimal.valueOf(2.0))
                                .precioUnitario(BigDecimal.valueOf(20.0))
                                .hcLinks(List.of(HCItemOCItemLinkDTO.builder().hcItemId(20L)
                                                .cantidadAsignada(BigDecimal.valueOf(2.0)).build()))
                                .build();

                assertThatThrownBy(() -> service.agregarItem(1L, itemDTO))
                                .isInstanceOf(BusinessRuleException.class)
                                .hasMessageContaining("excede el precio de referencia costeado");

                verify(ordenCompraRepository, never()).save(any());
        }

        @Test
        @DisplayName("actualizar item exitosamente validando precio costeado")
        void actualizarItemExitoso() {
                List<HCItemOCItemLink> link = List.of(new HCItemOCItemLink(20L, 10L, BigDecimal.TEN));
                OrdenCompraItem originalConLink = new OrdenCompraItem(10L, 1L, "INSUMO", 100, "Insumo Test",
                                BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN,
                                BigDecimal.valueOf(15.0), BigDecimal.valueOf(150.0), link);

                OrdenCompra ocConLink = new OrdenCompra(1L, new DocumentNumber("OC-001"), 2L, EstadoOC.EMITIDA,
                                LocalDate.now(), LocalDate.now().plusDays(5), "Obs",
                                BigDecimal.valueOf(150.0), List.of(originalConLink));

                when(ordenCompraRepository.findById(1L)).thenReturn(Optional.of(ocConLink));
                when(ordenCompraRepository.save(any(OrdenCompra.class))).thenAnswer(inv -> inv.getArgument(0));

                HojaCompraItem hcItem = new HojaCompraItem(20L, 5L, "INSUMO", 100, "Insumo Test",
                                BigDecimal.ONE, 10, BigDecimal.TEN, BigDecimal.valueOf(18.0), 2L, "PROV", null, null);
                HojaCompra hc = new HojaCompra(5L, new DocumentNumber("HC-01"), 1L, 10L, EstadoHC.APROBADA,
                                LocalDate.now(), null, List.of(hcItem));
                when(hojaCompraRepository.findAllByItemIds(List.of(20L))).thenReturn(List.of(hc));

                OrdenCompraItemDTO updateDTO = OrdenCompraItemDTO.builder()
                                .precioUnitario(BigDecimal.valueOf(17.0))
                                .cantidadComprada(BigDecimal.valueOf(10.0))
                                .build();

                OrdenCompraDTO res = service.actualizarItem(1L, 10L, updateDTO);

                assertThat(res.getItems().get(0).getPrecioUnitario()).isEqualByComparingTo(BigDecimal.valueOf(17.0));
                assertThat(res.getTotalNeto()).isEqualByComparingTo(BigDecimal.valueOf(170.0));
        }

        @Test
        @DisplayName("eliminar item de la OC exitosamente")
        void eliminarItemExitoso() {
                when(ordenCompraRepository.findById(1L)).thenReturn(Optional.of(ocEmitida));
                when(ordenCompraRepository.save(any(OrdenCompra.class))).thenAnswer(inv -> inv.getArgument(0));

                OrdenCompraDTO res = service.eliminarItem(1L, 10L);

                assertThat(res.getItems()).isEmpty();
                assertThat(res.getTotalNeto()).isEqualByComparingTo(BigDecimal.ZERO);
        }
}
