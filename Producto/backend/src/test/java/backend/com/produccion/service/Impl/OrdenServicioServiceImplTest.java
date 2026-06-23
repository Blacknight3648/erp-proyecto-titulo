package backend.com.produccion.service.Impl;

import backend.com.produccion.application.UseCase.CrearOSUseCase;
import backend.com.produccion.application.dto.CrearOSRequest;
import backend.com.produccion.application.dto.DespachoOSDTO;
import backend.com.produccion.application.dto.OrdenServicioDTO;
import backend.com.produccion.application.dto.RecepcionOSDTO;
import backend.com.produccion.application.service.impl.OrdenServicioServiceImpl;
import backend.com.produccion.domain.enums.EstadoOS;
import backend.com.produccion.domain.enums.TipoServicioOS;
import backend.com.produccion.domain.model.DespachoOS;
import backend.com.produccion.domain.model.OrdenServicio;
import backend.com.produccion.domain.repository.OrdenServicioRepository;
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
@DisplayName("OrdenServicioServiceImpl")
class OrdenServicioServiceImplTest {

    @Mock
    private OrdenServicioRepository ordenServicioRepository;

    @Mock
    private CrearOSUseCase crearOSUseCase;

    @InjectMocks
    private OrdenServicioServiceImpl ordenServicioService;

    // --- Métodos Helper ---

    private OrdenServicio os(Long id, String numero, Long opId, Long proveedorId, EstadoOS estado,
            Integer cantidadPactada, List<DespachoOS> despachos) {
        return new OrdenServicio(
                id,
                numero != null ? new DocumentNumber(numero) : null,
                opId,
                proveedorId,
                TipoServicioOS.BORDADO,
                estado,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "Trabajo de bordado",
                cantidadPactada,
                new BigDecimal("10.00"),
                null,
                "Observación test",
                despachos != null ? despachos : new ArrayList<>(),
                new ArrayList<>());
    }

    // --- crear ---

    @Test
    @DisplayName("crear delega en CrearOSUseCase y mapea el DTO resultante")
    void crear_delegaEnUseCaseYMapeaDTO() {
        CrearOSRequest request = new CrearOSRequest();
        OrdenServicio osMock = os(1L, "OS-001", 10L, 20L, EstadoOS.EMITIDA, 100, null);

        when(crearOSUseCase.ejecutar(request)).thenReturn(osMock);

        OrdenServicioDTO resultado = ordenServicioService.crear(request);

        assertThat(resultado.getIdOS()).isEqualTo(1L);
        assertThat(resultado.getNumeroOS()).isEqualTo("OS-001");
        assertThat(resultado.getEstado()).isEqualTo(EstadoOS.EMITIDA);
        verify(crearOSUseCase).ejecutar(request);
    }

    // --- registrarDespacho ---

    @Test
    @DisplayName("registrarDespacho agrega el despacho y transiciona la OS a EN_PROCESO")
    void registrarDespacho_agregaDespachoYTransicionaAEnProceso() {
        OrdenServicio osMock = os(1L, "OS-001", 10L, 20L, EstadoOS.EMITIDA, 100, null);
        DespachoOSDTO despachoDTO = DespachoOSDTO.builder()
                .fechaDespacho(LocalDate.now())
                .cantidadDespachada(50)
                .descripcion("Despacho parcial")
                .responsable("Juan")
                .observaciones("ok")
                .build();

        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(osMock));
        when(ordenServicioRepository.save(any(OrdenServicio.class))).thenAnswer(i -> i.getArgument(0));

        OrdenServicioDTO resultado = ordenServicioService.registrarDespacho(1L, despachoDTO);

        assertThat(resultado.getEstado()).isEqualTo(EstadoOS.EN_PROCESO);
        assertThat(resultado.getDespachos()).hasSize(1);
        assertThat(resultado.getDespachos().get(0).getCantidadDespachada()).isEqualTo(50);
        verify(ordenServicioRepository).save(osMock);
    }

    @Test
    @DisplayName("registrarDespacho lanza IllegalStateException si excede la cantidad pactada")
    void registrarDespacho_excedeCantidadPactada_lanzaIllegalStateException() {
        OrdenServicio osMock = os(1L, "OS-001", 10L, 20L, EstadoOS.EMITIDA, 10, null);
        DespachoOSDTO despachoDTO = DespachoOSDTO.builder()
                .fechaDespacho(LocalDate.now())
                .cantidadDespachada(20)
                .build();

        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(osMock));

        assertThatThrownBy(() -> ordenServicioService.registrarDespacho(1L, despachoDTO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("excede la cantidad pactada");
    }

    @Test
    @DisplayName("registrarDespacho lanza EntityNotFoundException si la OS no existe")
    void registrarDespacho_noExistente_lanzaEntityNotFoundException() {
        DespachoOSDTO despachoDTO = DespachoOSDTO.builder().cantidadDespachada(10).build();

        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ordenServicioService.registrarDespacho(1L, despachoDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Orden de Servicio no encontrada: 1");
    }

    // --- registrarRecepcion ---

    @Test
    @DisplayName("registrarRecepcion agrega la recepcion y transiciona la OS a RECEPCIONADA")
    void registrarRecepcion_agregaRecepcionYTransicionaARecepcionada() {
        DespachoOS despachoPrevio = new DespachoOS(1L, 1L, LocalDate.now(), 100, "Despacho total", "Juan", "ok");
        OrdenServicio osMock = os(1L, "OS-001", 10L, 20L, EstadoOS.EN_PROCESO, 100, List.of(despachoPrevio));
        RecepcionOSDTO recepcionDTO = RecepcionOSDTO.builder()
                .fechaRecepcion(LocalDate.now())
                .cantidadRecibida(100)
                .cantidadConforme(95)
                .cantidadDefectuosa(5)
                .responsable("Pedro")
                .observaciones("ok")
                .build();

        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(osMock));
        when(ordenServicioRepository.save(any(OrdenServicio.class))).thenAnswer(i -> i.getArgument(0));

        OrdenServicioDTO resultado = ordenServicioService.registrarRecepcion(1L, recepcionDTO);

        assertThat(resultado.getEstado()).isEqualTo(EstadoOS.RECEPCIONADA);
        assertThat(resultado.getRecepciones()).hasSize(1);
        assertThat(resultado.getRecepciones().get(0).getCantidadRecibida()).isEqualTo(100);
        verify(ordenServicioRepository).save(osMock);
    }

    @Test
    @DisplayName("registrarRecepcion lanza IllegalStateException si la OS aun esta EMITIDA")
    void registrarRecepcion_osEmitida_lanzaIllegalStateException() {
        OrdenServicio osMock = os(1L, "OS-001", 10L, 20L, EstadoOS.EMITIDA, 100, null);
        RecepcionOSDTO recepcionDTO = RecepcionOSDTO.builder().cantidadRecibida(10).build();

        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(osMock));

        assertThatThrownBy(() -> ordenServicioService.registrarRecepcion(1L, recepcionDTO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("sin despachos previos");
    }

    // --- cerrar ---

    @Test
    @DisplayName("cerrar transiciona la OS RECEPCIONADA a CERRADA y persiste")
    void cerrar_transicionaACerradaYPersiste() {
        OrdenServicio osMock = os(1L, "OS-001", 10L, 20L, EstadoOS.RECEPCIONADA, 100, null);

        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(osMock));
        when(ordenServicioRepository.save(any(OrdenServicio.class))).thenAnswer(i -> i.getArgument(0));

        OrdenServicioDTO resultado = ordenServicioService.cerrar(1L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoOS.CERRADA);
        verify(ordenServicioRepository).save(osMock);
    }

    @Test
    @DisplayName("cerrar lanza IllegalStateException si la transicion de estado no es valida")
    void cerrar_estadoInvalido_lanzaIllegalStateException() {
        OrdenServicio osMock = os(1L, "OS-001", 10L, 20L, EstadoOS.EMITIDA, 100, null);

        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(osMock));

        assertThatThrownBy(() -> ordenServicioService.cerrar(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Transición inválida");
    }

    @Test
    @DisplayName("cerrar lanza EntityNotFoundException si la OS no existe")
    void cerrar_noExistente_lanzaEntityNotFoundException() {
        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ordenServicioService.cerrar(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Orden de Servicio no encontrada: 1");
    }

    // --- consultas ---

    @Test
    @DisplayName("obtenerPorId retorna DTO si existe")
    void obtenerPorId_existente_retornaDTOOptional() {
        OrdenServicio osMock = os(1L, "OS-001", 10L, 20L, EstadoOS.EMITIDA, 100, null);
        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(osMock));

        Optional<OrdenServicioDTO> resultado = ordenServicioService.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdOS()).isEqualTo(1L);
    }

    @Test
    @DisplayName("obtenerPorId retorna Optional vacio si no existe")
    void obtenerPorId_noExistente_retornaOptionalEmpty() {
        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<OrdenServicioDTO> resultado = ordenServicioService.obtenerPorId(1L);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("listarTodas retorna el mapeo de toda la lista")
    void listarTodas_retornaListaDeDTOs() {
        when(ordenServicioRepository.findAll()).thenReturn(List.of(
                os(1L, "OS-001", 10L, 20L, EstadoOS.EMITIDA, 100, null),
                os(2L, "OS-002", 11L, 21L, EstadoOS.CERRADA, 50, null)));

        List<OrdenServicioDTO> resultado = ordenServicioService.listarTodas();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNumeroOS()).isEqualTo("OS-001");
        assertThat(resultado.get(1).getNumeroOS()).isEqualTo("OS-002");
    }

    @Test
    @DisplayName("listarPorEstado filtra y mapea los registros por estado")
    void listarPorEstado_retornaRegistrosFiltrados() {
        when(ordenServicioRepository.findAllByEstado(EstadoOS.EMITIDA)).thenReturn(List.of(
                os(1L, "OS-001", 10L, 20L, EstadoOS.EMITIDA, 100, null)));

        List<OrdenServicioDTO> resultado = ordenServicioService.listarPorEstado(EstadoOS.EMITIDA);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo(EstadoOS.EMITIDA);
        verify(ordenServicioRepository).findAllByEstado(EstadoOS.EMITIDA);
    }

    @Test
    @DisplayName("listarPorOP filtra y mapea los registros por OP")
    void listarPorOP_retornaRegistrosFiltrados() {
        when(ordenServicioRepository.findAllByOpId(10L)).thenReturn(List.of(
                os(1L, "OS-001", 10L, 20L, EstadoOS.EMITIDA, 100, null)));

        List<OrdenServicioDTO> resultado = ordenServicioService.listarPorOP(10L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getOpId()).isEqualTo(10L);
        verify(ordenServicioRepository).findAllByOpId(10L);
    }

    @Test
    @DisplayName("listarPorProveedor filtra y mapea los registros por proveedor")
    void listarPorProveedor_retornaRegistrosFiltrados() {
        when(ordenServicioRepository.findAllByProveedorId(20L)).thenReturn(List.of(
                os(1L, "OS-001", 10L, 20L, EstadoOS.EMITIDA, 100, null)));

        List<OrdenServicioDTO> resultado = ordenServicioService.listarPorProveedor(20L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProveedorId()).isEqualTo(20L);
        verify(ordenServicioRepository).findAllByProveedorId(20L);
    }
}
