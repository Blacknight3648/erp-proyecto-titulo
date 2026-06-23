package backend.com.produccion.service.Impl;

import backend.com.produccion.application.UseCase.GenerarHCDesdeOPUseCase;
import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.application.service.impl.HojaCompraServiceImpl;
import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests de HojaCompraServiceImpl con dependencias mockeadas (Mockito).
 * Verifica la correcta delegación a los casos de uso, repositorios,
 * mutación de estados (aprobar/cerrar) y el correcto mapeo de las entidades a DTOs.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HojaCompraServiceImpl")
class HojaCompraServiceImplTest {

    @Mock
    private HojaCompraRepository hojaCompraRepository;

    @Mock
    private GenerarHCDesdeOPUseCase generarHCDesdeOPUseCase;

    @InjectMocks
    private HojaCompraServiceImpl hojaCompraService;

    // Métodos Helper para instanciar objetos de dominio de manera compacta
    private HojaCompra hc(Long id, String numero, Long opId, EstadoHC estado) {
        return new HojaCompra(
            id,
            numero != null ? new DocumentNumber(numero) : null,
            opId,
            100L,
            estado,
            java.time.LocalDate.now(),
            "Obs test",
            new ArrayList<>()
        );
    }

    private HojaCompraItem item(Long id, Long hcId, String nombre, BigDecimal precio) {
        return new HojaCompraItem(
            id,
            hcId,
            "TELA",
            200,
            nombre,
            new BigDecimal("1.5"),
            100,
            new BigDecimal("150"),
            precio,
            300L,
            "Proveedor S.A.",
            null,
            null
        );
    }

    @Test
    @DisplayName("generar desde OP delega en UseCase y mapea DTO correctamente")
    void generarDesdeOP_delegaEnUseCaseYMapeaDTO() {
        HojaCompra hcMock = hc(1L, "HC-001", 10L, EstadoHC.BORRADOR);
        hcMock.addItem(item(100L, 1L, "Boton", new BigDecimal("0.50")));

        when(generarHCDesdeOPUseCase.ejecutar(10L)).thenReturn(hcMock);

        HojaCompraDTO resultado = hojaCompraService.generarDesdeOP(10L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdHC()).isEqualTo(1L);
        assertThat(resultado.getNumeroHC()).isEqualTo("HC-001");
        assertThat(resultado.getOpId()).isEqualTo(10L);
        assertThat(resultado.getItems()).hasSize(1);
        assertThat(resultado.getItems().get(0).getNombreInsumo()).isEqualTo("Boton");
        verify(generarHCDesdeOPUseCase).ejecutar(10L);
    }

    @Test
    @DisplayName("aprobar hoja de compra muta el estado a APROBADA y persiste")
    void aprobar_cambiaEstadoAAprobadaYPersiste() {
        HojaCompra hcMock = hc(1L, "HC-001", 10L, EstadoHC.BORRADOR);

        when(hojaCompraRepository.findById(1L)).thenReturn(Optional.of(hcMock));
        // Simulamos el comportamiento del save retornando la misma entidad ya mutada externamente
        when(hojaCompraRepository.save(any(HojaCompra.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HojaCompraDTO resultado = hojaCompraService.aprobar(1L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoHC.APROBADA);
        verify(hojaCompraRepository).findById(1L);
        verify(hojaCompraRepository).save(hcMock);
    }

    @Test
    @DisplayName("aprobar lanza EntityNotFoundException si la HC no existe")
    void aprobar_noExistente_lanzaException() {
        when(hojaCompraRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hojaCompraService.aprobar(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Hoja de Compra no encontrada: 1");
    }

    @Test
    @DisplayName("cerrar hoja de compra muta el estado a CERRADA y persiste")
    void cerrar_cambiaEstadoACerradaYPersiste() {
        HojaCompra hcMock = hc(1L, "HC-001", 10L, EstadoHC.APROBADA);

        when(hojaCompraRepository.findById(1L)).thenReturn(Optional.of(hcMock));
        when(hojaCompraRepository.save(any(HojaCompra.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HojaCompraDTO resultado = hojaCompraService.cerrar(1L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoHC.CERRADA);
        verify(hojaCompraRepository).findById(1L);
        verify(hojaCompraRepository).save(hcMock);
    }

    @Test
    @DisplayName("cerrar lanza EntityNotFoundException si la HC no existe")
    void cerrar_noExistente_lanzaException() {
        when(hojaCompraRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hojaCompraService.cerrar(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Hoja de Compra no encontrada: 1");
    }

    @Test
    @DisplayName("obtener por id retorna DTO si existe")
    void obtenerPorId_siExiste_retornaDTOOptional() {
        HojaCompra hcMock = hc(1L, "HC-001", 10L, EstadoHC.BORRADOR);
        when(hojaCompraRepository.findById(1L)).thenReturn(Optional.of(hcMock));

        Optional<HojaCompraDTO> resultado = hojaCompraService.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdHC()).isEqualTo(1L);
    }

    @Test
    @DisplayName("obtener por id retorna Optional vacio si no existe")
    void obtenerPorId_noExiste_retornaOptionalEmpty() {
        when(hojaCompraRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<HojaCompraDTO> resultado = hojaCompraService.obtenerPorId(1L);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("obtener por OP id retorna DTO si existe")
    void obtenerPorOpId_siExiste_retornaDTOOptional() {
        HojaCompra hcMock = hc(1L, "HC-001", 10L, EstadoHC.BORRADOR);
        when(hojaCompraRepository.findByOpId(10L)).thenReturn(Optional.of(hcMock));

        Optional<HojaCompraDTO> resultado = hojaCompraService.obtenerPorOpId(10L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getOpId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("listar todas retorna mapeo de toda la lista")
    void listarTodas_retornaListaDeDTOs() {
        when(hojaCompraRepository.findAll()).thenReturn(List.of(
                hc(1L, "HC-001", 10L, EstadoHC.BORRADOR),
                hc(2L, "HC-002", 11L, EstadoHC.APROBADA)
        ));

        List<HojaCompraDTO> resultado = hojaCompraService.listarTodas();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNumeroHC()).isEqualTo("HC-001");
        assertThat(resultado.get(1).getNumeroHC()).isEqualTo("HC-002");
    }

    @Test
    @DisplayName("listar por estado filtra y mapea los registros de la base de datos")
    void listarPorEstado_retornaRegistrosFiltrados() {
        when(hojaCompraRepository.findAllByEstado(EstadoHC.BORRADOR)).thenReturn(List.of(
                hc(1L, "HC-001", 10L, EstadoHC.BORRADOR)
        ));

        List<HojaCompraDTO> resultado = hojaCompraService.listarPorEstado(EstadoHC.BORRADOR);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo(EstadoHC.BORRADOR);
        verify(hojaCompraRepository).findAllByEstado(EstadoHC.BORRADOR);
    }
}
