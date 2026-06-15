package backend.com.comercial.service;

import backend.com.comercial.application.dto.DescripcionPlantillaDTO;
import backend.com.comercial.application.dto.SCOSAccesorioDTO;
import backend.com.comercial.application.dto.SCOSLogotipoDTO;
import backend.com.comercial.application.dto.SCOSTelaDTO;
import backend.com.comercial.application.dto.SolicitudCostosCreateDTO;
import backend.com.comercial.application.dto.SolicitudCostosDTO;
import backend.com.comercial.application.service.DescripcionPlantillaService;
import backend.com.comercial.application.service.impl.SolicitudCostosServiceImpl;
import backend.com.comercial.domain.enums.EstadoSCOS;
import backend.com.comercial.domain.model.SolicitudCostos;
import backend.com.comercial.domain.repository.DescripcionPlantillaRepository;
import backend.com.comercial.domain.repository.SolicitudCostosRepository;
import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.service.CosteoService;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
@DisplayName("SolicitudCostosServiceImpl")
class SolicitudCostosServiceImplTest {

    @Mock
    private SolicitudCostosRepository repository;

    @Mock
    private CosteoService costeoService;

    @Mock
    private DescripcionPlantillaService descripcionPlantillaService;

    @Mock
    private DescripcionPlantillaRepository descripcionPlantillaRepository;

    @InjectMocks
    private SolicitudCostosServiceImpl solicitudCostosServiceImpl;

    // ---------------- HELPERS ----------------

    private SolicitudCostos solicitudCostos(Long id, String numero, EstadoSCOS estado, Integer cantidad) {
        return new SolicitudCostos(id, new DocumentNumber(numero), estado, "SCOS", 1L, "Cliente Uno", 2L,
                "Vendedor Uno", "Polera", "Polera basica", false, false, cantidad, "M", "S-XL", LocalDate.now(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), BigDecimal.ZERO);
    }

    /** Simula la persistencia: asigna idSCOS a la entidad recibida, preservando el resto de los datos. */
    private SolicitudCostos persistido(SolicitudCostos d, Long idAsignado) {
        return new SolicitudCostos(idAsignado, d.getNumeroSCOS(), d.getEstado(), d.getTipo(), d.getClienteId(),
                d.getClienteNombre(), d.getVendedorId(), d.getVendedorNombre(), d.getArticuloDescripcion(),
                d.getNombrePrenda(), d.getEsMuestra(), d.getHasLogo(), d.getCantidad(), d.getGenero(),
                d.getTallaje(), d.getFecha(), d.getTelas(), d.getAccesorios(), d.getLogotipos(),
                d.getDescripciones(), d.getCostoTotal());
    }

    // ---------------- create ----------------

    @Test
    @DisplayName("create genera el número correlativo, persiste detalles y crea el pre-costeo")
    void create_ok() {
        SCOSTelaDTO telaDto = SCOSTelaDTO.builder()
                .aplicacion("Cuerpo")
                .nombre("Algodon")
                .proveedorReferencia("PROV-1")
                .composicion("100% algodon")
                .color("Blanco")
                .peso(BigDecimal.ONE)
                .consumo(BigDecimal.valueOf(2))
                .unidadMedida("mts")
                .precioUnitario(BigDecimal.TEN)
                .build();

        SCOSAccesorioDTO accesorioDto = SCOSAccesorioDTO.builder()
                .tipo("Cierre")
                .nombreAccesorio("Cierre metalico")
                .proveedorReferencia("PROV-2")
                .cantidad(2)
                .consumo(BigDecimal.ONE)
                .unidadMedida("un")
                .precioUnitario(BigDecimal.valueOf(500))
                .build();

        SCOSLogotipoDTO logotipoDto = SCOSLogotipoDTO.builder()
                .tipo("Bordado")
                .nombre("Logo")
                .ubicacion("Pecho")
                .color("Negro")
                .tamanio(5.0)
                .cantidad(1)
                .precio(BigDecimal.valueOf(1000))
                .build();

        SolicitudCostosCreateDTO dto = SolicitudCostosCreateDTO.builder()
                .clienteId(1L)
                .vendedorId(2L)
                .articuloDescripcion("Polera")
                .nombrePrenda("Polera basica")
                .cantidad(10)
                .genero("M")
                .tallaje("S-XL")
                .tipo("SCOS")
                .telas(List.of(telaDto))
                .accesorios(List.of(accesorioDto))
                .logotipos(List.of(logotipoDto))
                .descripciones(new ArrayList<>())
                .build();

        when(repository.countByTipo("SCOS")).thenReturn(3L);
        when(repository.save(any(SolicitudCostos.class)))
                .thenAnswer(invocation -> persistido(invocation.getArgument(0), 1L));
        when(costeoService.findBySolicitudCostosId(1L)).thenReturn(Optional.empty());
        when(costeoService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(descripcionPlantillaService.listarPorSCOS(1L)).thenReturn(new ArrayList<>());

        SolicitudCostosDTO resultado = solicitudCostosServiceImpl.create(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNumero()).isEqualTo("SCOS-0004");
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        assertThat(resultado.getMoneda()).isEqualTo("CLP");

        assertThat(resultado.getTelas()).hasSize(1);
        assertThat(resultado.getTelas().get(0).getNombre()).isEqualTo("Algodon");
        assertThat(resultado.getTelas().get(0).getPrecioUnitario()).isEqualTo(BigDecimal.TEN);

        assertThat(resultado.getAccesorios()).hasSize(1);
        assertThat(resultado.getAccesorios().get(0).getNombreAccesorio()).isEqualTo("Cierre metalico");

        assertThat(resultado.getLogotipos()).hasSize(1);
        assertThat(resultado.getLogotipos().get(0).getNombre()).isEqualTo("Logo");

        ArgumentCaptor<CosteoDTO> costeoCaptor = ArgumentCaptor.forClass(CosteoDTO.class);
        verify(costeoService).save(costeoCaptor.capture());
        assertThat(costeoCaptor.getValue().getNumeroCosteo()).isEqualTo("PRE-SCOS-0004");
        assertThat(costeoCaptor.getValue().getSolicitudCostosId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("create no sobrescribe el número de un pre-costeo ya existente")
    void create_noSobreescribeCosteoExistente() {
        SolicitudCostosCreateDTO dto = SolicitudCostosCreateDTO.builder()
                .clienteId(1L)
                .articuloDescripcion("Polera")
                .cantidad(10)
                .genero("M")
                .build();

        CosteoDTO costeoExistente = CosteoDTO.builder()
                .idCosteo(99L)
                .numeroCosteo("COST-0099")
                .solicitudCostosId(1L)
                .build();

        when(repository.countByTipo("SCOS")).thenReturn(0L);
        when(repository.save(any(SolicitudCostos.class)))
                .thenAnswer(invocation -> persistido(invocation.getArgument(0), 1L));
        when(costeoService.findBySolicitudCostosId(1L)).thenReturn(Optional.of(costeoExistente));
        when(costeoService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(descripcionPlantillaService.listarPorSCOS(1L)).thenReturn(new ArrayList<>());

        solicitudCostosServiceImpl.create(dto);

        ArgumentCaptor<CosteoDTO> costeoCaptor = ArgumentCaptor.forClass(CosteoDTO.class);
        verify(costeoService).save(costeoCaptor.capture());
        assertThat(costeoCaptor.getValue().getNumeroCosteo()).isEqualTo("COST-0099");
        assertThat(costeoCaptor.getValue().getIdCosteo()).isEqualTo(99L);
    }

    @Test
    @DisplayName("create persiste las descripciones asignándoles el idSCOS generado")
    void create_persisteDescripciones() {
        DescripcionPlantillaDTO descripcionDto = DescripcionPlantillaDTO.builder()
                .idPlantilla(5L)
                .valorDescripcion("polar")
                .build();

        SolicitudCostosCreateDTO dto = SolicitudCostosCreateDTO.builder()
                .clienteId(1L)
                .articuloDescripcion("Polera")
                .cantidad(10)
                .genero("M")
                .descripciones(List.of(descripcionDto))
                .build();

        when(repository.countByTipo("SCOS")).thenReturn(0L);
        when(repository.save(any(SolicitudCostos.class)))
                .thenAnswer(invocation -> persistido(invocation.getArgument(0), 1L));
        when(costeoService.findBySolicitudCostosId(1L)).thenReturn(Optional.empty());
        when(costeoService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(descripcionPlantillaService.listarPorSCOS(1L)).thenReturn(new ArrayList<>());

        solicitudCostosServiceImpl.create(dto);

        verify(descripcionPlantillaService).crear(descripcionDto);
        assertThat(descripcionDto.getIdSCOS()).isEqualTo(1L);
    }

    // ---------------- update ----------------

    @Test
    @DisplayName("update actualiza los datos, reemplaza descripciones y regenera el pre-costeo")
    void update_ok() {
        SolicitudCostos existente = solicitudCostos(1L, "SCOS-0001", EstadoSCOS.PENDIENTE, 10);

        SolicitudCostosCreateDTO dto = SolicitudCostosCreateDTO.builder()
                .estado("APROBADA")
                .articuloDescripcion("Polera actualizada")
                .cantidad(20)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(SolicitudCostos.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(costeoService.findBySolicitudCostosId(1L)).thenReturn(Optional.empty());
        when(costeoService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(descripcionPlantillaService.listarPorSCOS(1L)).thenReturn(new ArrayList<>());

        SolicitudCostosDTO resultado = solicitudCostosServiceImpl.update(1L, dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo("APROBADA");
        assertThat(resultado.getArticuloDescripcion()).isEqualTo("Polera actualizada");
        assertThat(resultado.getCantidad()).isEqualTo(20);

        verify(descripcionPlantillaRepository).deleteByIdSCOS(1L);

        ArgumentCaptor<SolicitudCostos> captor = ArgumentCaptor.forClass(SolicitudCostos.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getEstado()).isEqualTo(EstadoSCOS.APROBADA);
        assertThat(captor.getValue().getArticuloDescripcion()).isEqualTo("Polera actualizada");
        assertThat(captor.getValue().getCantidad()).isEqualTo(20);
    }

    @Test
    @DisplayName("update conserva los valores existentes cuando el DTO no envía cambios")
    void update_conservaValoresExistentes() {
        SolicitudCostos existente = solicitudCostos(1L, "SCOS-0001", EstadoSCOS.PENDIENTE, 10);

        SolicitudCostosCreateDTO dto = SolicitudCostosCreateDTO.builder().build();

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(SolicitudCostos.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(costeoService.findBySolicitudCostosId(1L)).thenReturn(Optional.empty());
        when(costeoService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(descripcionPlantillaService.listarPorSCOS(1L)).thenReturn(new ArrayList<>());

        SolicitudCostosDTO resultado = solicitudCostosServiceImpl.update(1L, dto);

        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        assertThat(resultado.getArticuloDescripcion()).isEqualTo("Polera");
        assertThat(resultado.getCantidad()).isEqualTo(10);
    }

    @Test
    @DisplayName("update lanza excepción si la solicitud no existe")
    void update_noExiste() {
        SolicitudCostosCreateDTO dto = SolicitudCostosCreateDTO.builder().build();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitudCostosServiceImpl.update(1L, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(repository, never()).save(any());
        verify(descripcionPlantillaRepository, never()).deleteByIdSCOS(any());
    }

    // ---------------- findById ----------------

    @Test
    @DisplayName("findById retorna el DTO mapeado cuando la solicitud existe")
    void findById_ok() {
        SolicitudCostos existente = solicitudCostos(1L, "SCOS-0001", EstadoSCOS.PENDIENTE, 10);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(descripcionPlantillaService.listarPorSCOS(1L)).thenReturn(new ArrayList<>());

        Optional<SolicitudCostosDTO> resultado = solicitudCostosServiceImpl.findById(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
        assertThat(resultado.get().getNumero()).isEqualTo("SCOS-0001");
    }

    @Test
    @DisplayName("findById retorna Optional vacío cuando la solicitud no existe")
    void findById_noExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<SolicitudCostosDTO> resultado = solicitudCostosServiceImpl.findById(1L);

        assertThat(resultado).isEmpty();
    }

    // ---------------- findAll ----------------

    @Test
    @DisplayName("findAll retorna el mapeo de todas las solicitudes")
    void findAll_ok() {
        SolicitudCostos s1 = solicitudCostos(1L, "SCOS-0001", EstadoSCOS.PENDIENTE, 10);
        SolicitudCostos s2 = solicitudCostos(2L, "SCOS-0002", EstadoSCOS.APROBADA, 20);

        when(repository.findAll()).thenReturn(List.of(s1, s2));
        when(descripcionPlantillaService.listarPorSCOS(1L)).thenReturn(new ArrayList<>());
        when(descripcionPlantillaService.listarPorSCOS(2L)).thenReturn(new ArrayList<>());

        List<SolicitudCostosDTO> resultado = solicitudCostosServiceImpl.findAll();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNumero()).isEqualTo("SCOS-0001");
        assertThat(resultado.get(1).getNumero()).isEqualTo("SCOS-0002");
    }

    // ---------------- delete ----------------

    @Test
    @DisplayName("delete elimina las descripciones asociadas y la solicitud")
    void delete_ok() {
        solicitudCostosServiceImpl.delete(1L);

        verify(descripcionPlantillaRepository).deleteByIdSCOS(1L);
        verify(repository).deleteById(1L);
    }
}
