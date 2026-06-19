package backend.com.produccion.service.Impl;

import backend.com.comercial.domain.enums.EstadoSCOS;
import backend.com.comercial.domain.model.SolicitudCostos;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.domain.repository.SolicitudCostosRepository;
import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.dto.CosteoResumenEVNDTO;
import backend.com.produccion.application.service.impl.CosteoServiceImpl;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.CosteoItem;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.infrastructure.mapper.CosteoMapper;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.domain.model.ArticuloTela;
import backend.com.shared.domain.model.Composicion;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CosteoServiceImpl")
class CosteoServiceImplTest {

    @Mock
    private CosteoRepository repository;

    @Mock
    private CosteoMapper mapper;

    @Mock
    private SolicitudCostosRepository scosRepository;

    @Mock
    private ArticuloRepository articuloRepository;

    @Mock
    private EvaluacionNegocioRepository evnRepository;

    @Mock
    private backend.com.shared.application.service.NumeroDocumentoService numeroDocumentoService;

    @Mock
    private backend.com.produccion.application.UseCase.CrearVersionCosteoUseCase crearVersionCosteoUseCase;

    @Mock
    private backend.com.shared.application.service.HistorialEstadoService historialEstadoService;

    @InjectMocks
    private CosteoServiceImpl costeoService;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // save asigna el correlativo propio del Costeo (C-0000001) cuando no lo trae.
        org.mockito.Mockito.lenient().when(numeroDocumentoService.siguienteFormateado("C"))
                .thenReturn(new DocumentNumber("C-0000001"));
    }

    // --- Métodos Helper ---

    private Costeo costeo(Long id, Long scosId, List<CosteoItem> items) {
        Costeo c = new Costeo();
        c.setIdCosteo(id);
        c.setSolicitudCostosId(scosId);
        c.setItems(items != null ? items : new ArrayList<>());
        return c;
    }

    private SolicitudCostos solicitudCostos(Long id, Long clienteId, String clienteNombre,
            Long vendedorId, String vendedorNombre, String genero, String nombrePrenda) {
        return new SolicitudCostos(id, new DocumentNumber("SCOS-001"), EstadoSCOS.APROBADA, "PRODUCCION",
                clienteId, clienteNombre, vendedorId, vendedorNombre, "desc", nombrePrenda,
                false, false, 10, genero, "M", LocalDate.now(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), BigDecimal.ZERO);
    }

    private Articulo articuloConTela(Integer id, String nombre, String composicionDesc) {
        Composicion composicion = Composicion.builder()
                .idComposicion(1)
                .descripcionComposicion(composicionDesc)
                .build();
        ArticuloTela detalleTela = ArticuloTela.builder()
                .composicion(composicion)
                .build();
        return Articulo.builder()
                .idArticulo(id)
                .nombreArticulo(nombre)
                .detalleTela(detalleTela)
                .build();
    }

    // --- save ---

    @Test
    @DisplayName("save sin solicitud de costos no consulta el repositorio de SCOS")
    void save_sinSolicitudCostos_noConsultaScosRepository() {
        CosteoDTO dto = CosteoDTO.builder().idCosteo(1L).items(List.of()).build();
        Costeo domain = costeo(1L, null, new ArrayList<>());
        CosteoDTO expected = CosteoDTO.builder().idCosteo(1L).items(List.of()).build();

        when(mapper.toDomainFromDto(dto)).thenReturn(domain);
        when(repository.save(domain)).thenReturn(domain);
        when(mapper.toDto(domain)).thenReturn(expected);

        CosteoDTO resultado = costeoService.save(dto);

        assertThat(resultado).isEqualTo(expected);
        verify(scosRepository, never()).findById(any());
    }

    @Test
    @DisplayName("save con solicitud de costos enriquece cliente y vendedor antes de mapear")
    void save_conSolicitudCostos_enriqueceClienteYVendedor() {
        CosteoDTO dto = CosteoDTO.builder().idCosteo(1L).solicitudCostosId(5L).items(List.of()).build();
        Costeo domain = costeo(1L, 5L, new ArrayList<>());
        SolicitudCostos scos = solicitudCostos(5L, 100L, "Cliente X", 200L, "Vendedor Y", "Hombre", "Polera");

        when(mapper.toDomainFromDto(dto)).thenReturn(domain);
        when(repository.save(domain)).thenReturn(domain);
        when(scosRepository.findById(5L)).thenReturn(Optional.of(scos));
        when(mapper.toDto(any(Costeo.class))).thenReturn(CosteoDTO.builder().idCosteo(1L).build());

        costeoService.save(dto);

        ArgumentCaptor<Costeo> captor = ArgumentCaptor.forClass(Costeo.class);
        verify(mapper).toDto(captor.capture());
        Costeo enriquecido = captor.getValue();
        assertThat(enriquecido.getClienteId()).isEqualTo(100L);
        assertThat(enriquecido.getClienteNombre()).isEqualTo("Cliente X");
        assertThat(enriquecido.getVendedorId()).isEqualTo(200L);
        assertThat(enriquecido.getVendedorNombre()).isEqualTo("Vendedor Y");
    }

    @Test
    @DisplayName("save enriquece el Articulo de los items TELAS con articuloId")
    void save_itemTelaConArticuloId_enriqueceArticulo() {
        CosteoItem item = CosteoItem.builder().tipoInsumo("TELAS").articuloId(50).nombreInsumo("Tela X").build();
        Costeo domain = costeo(1L, null, List.of(item));
        Articulo articulo = articuloConTela(50, "Algodón Pima", "100% Algodón");

        CosteoDTO dto = CosteoDTO.builder().idCosteo(1L).items(List.of()).build();
        when(mapper.toDomainFromDto(dto)).thenReturn(domain);
        when(repository.save(domain)).thenReturn(domain);
        when(articuloRepository.findById(50)).thenReturn(Optional.of(articulo));
        when(mapper.toDto(any(Costeo.class))).thenReturn(CosteoDTO.builder().idCosteo(1L).build());

        costeoService.save(dto);

        ArgumentCaptor<Costeo> captor = ArgumentCaptor.forClass(Costeo.class);
        verify(mapper).toDto(captor.capture());
        assertThat(captor.getValue().getItems().get(0).getArticulo()).isEqualTo(articulo);
    }

    @Test
    @DisplayName("save no consulta el repositorio de Articulo para items que no son TELAS ni ACCESORIOS")
    void save_itemNoTelaNiAccesorio_noConsultaArticuloRepository() {
        CosteoItem item = CosteoItem.builder().tipoInsumo("LOGOTIPO").articuloId(50).nombreInsumo("Logo").build();
        Costeo domain = costeo(1L, null, List.of(item));

        CosteoDTO dto = CosteoDTO.builder().idCosteo(1L).items(List.of()).build();
        when(mapper.toDomainFromDto(dto)).thenReturn(domain);
        when(repository.save(domain)).thenReturn(domain);
        when(mapper.toDto(any(Costeo.class))).thenReturn(CosteoDTO.builder().idCosteo(1L).build());

        costeoService.save(dto);

        verify(articuloRepository, never()).findById(any());
    }

    @Test
    @DisplayName("save actualizando costeo preserva estado y version si no vienen en el DTO")
    void save_actualizandoCosteo_preservaEstadoYVersionDeLaBD() {
        CosteoDTO dto = CosteoDTO.builder().idCosteo(1L).items(List.of()).build(); // Sin estado ni versión
        Costeo domain = costeo(1L, null, new ArrayList<>());
        domain.setEstado(backend.com.produccion.domain.enums.EstadoCosteo.BORRADOR); // valor mapeado por defecto
        domain.setVersion(1); // valor mapeado por defecto

        Costeo existingInDb = costeo(1L, null, new ArrayList<>());
        existingInDb.setEstado(backend.com.produccion.domain.enums.EstadoCosteo.RECHAZADO);
        existingInDb.setVersion(3);
        existingInDb.setMotivoRechazo("Faltan insumos");

        when(mapper.toDomainFromDto(dto)).thenReturn(domain);
        when(repository.findById(1L)).thenReturn(Optional.of(existingInDb));
        when(repository.save(domain)).thenReturn(domain);
        when(mapper.toDto(any(Costeo.class))).thenReturn(CosteoDTO.builder().idCosteo(1L).build());

        costeoService.save(dto);

        ArgumentCaptor<Costeo> captor = ArgumentCaptor.forClass(Costeo.class);
        verify(repository).save(captor.capture());
        Costeo saved = captor.getValue();
        assertThat(saved.getEstado()).isEqualTo(backend.com.produccion.domain.enums.EstadoCosteo.RECHAZADO);
        assertThat(saved.getVersion()).isEqualTo(3);
        assertThat(saved.getMotivoRechazo()).isEqualTo("Faltan insumos");
    }

    // --- findAll / findBySolicitudCostosId / findAllBySolicitudCostosId ---

    @Test
    @DisplayName("findAll mapea todos los costeos del repositorio")
    void findAll_mapeaTodosLosCosteos() {
        Costeo costeo1 = costeo(1L, null, new ArrayList<>());
        Costeo costeo2 = costeo(2L, null, new ArrayList<>());
        CosteoDTO dto1 = CosteoDTO.builder().idCosteo(1L).build();
        CosteoDTO dto2 = CosteoDTO.builder().idCosteo(2L).build();

        when(repository.findAll()).thenReturn(List.of(costeo1, costeo2));
        when(mapper.toDto(costeo1)).thenReturn(dto1);
        when(mapper.toDto(costeo2)).thenReturn(dto2);

        List<CosteoDTO> resultado = costeoService.findAll();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    @Test
    @DisplayName("findBySolicitudCostosId retorna DTO si existe")
    void findBySolicitudCostosId_existente_retornaDTOOptional() {
        Costeo domain = costeo(1L, 5L, new ArrayList<>());
        CosteoDTO dto = CosteoDTO.builder().idCosteo(1L).solicitudCostosId(5L).build();

        when(repository.findBySolicitudCostosId(5L)).thenReturn(Optional.of(domain));
        when(mapper.toDto(domain)).thenReturn(dto);

        Optional<CosteoDTO> resultado = costeoService.findBySolicitudCostosId(5L);

        assertThat(resultado).contains(dto);
    }

    @Test
    @DisplayName("findBySolicitudCostosId retorna Optional vacio si no existe")
    void findBySolicitudCostosId_noExistente_retornaOptionalEmpty() {
        when(repository.findBySolicitudCostosId(5L)).thenReturn(Optional.empty());

        Optional<CosteoDTO> resultado = costeoService.findBySolicitudCostosId(5L);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("findAllBySolicitudCostosId mapea la lista de costeos asociados")
    void findAllBySolicitudCostosId_mapeaLista() {
        Costeo domain = costeo(1L, 5L, new ArrayList<>());
        CosteoDTO dto = CosteoDTO.builder().idCosteo(1L).solicitudCostosId(5L).build();

        when(repository.findAllBySolicitudCostosId(5L)).thenReturn(List.of(domain));
        when(mapper.toDto(domain)).thenReturn(dto);

        List<CosteoDTO> resultado = costeoService.findAllBySolicitudCostosId(5L);

        assertThat(resultado).containsExactly(dto);
    }

    // --- obtenerDisponiblesParaEVN ---

    @Test
    @DisplayName("obtenerDisponiblesParaEVN filtra los no vinculados y solo expone costeos APROBADOS")
    void obtenerDisponiblesParaEVN_filtraCosteosVinculados() {
        Costeo costeo1 = costeo(1L, null, new ArrayList<>());
        costeo1.setEstado(backend.com.produccion.domain.enums.EstadoCosteo.APROBADO); // disponible
        Costeo costeo2 = costeo(2L, null, new ArrayList<>());
        costeo2.setEstado(backend.com.produccion.domain.enums.EstadoCosteo.APROBADO); // pero vinculado
        CosteoDTO dto1 = CosteoDTO.builder().idCosteo(1L).build();

        when(evnRepository.findLinkedCosteoIds()).thenReturn(List.of(2L));
        when(repository.findAll()).thenReturn(List.of(costeo1, costeo2));
        when(mapper.toDto(costeo1)).thenReturn(dto1);

        List<CosteoDTO> resultado = costeoService.obtenerDisponiblesParaEVN();

        assertThat(resultado).containsExactly(dto1);
        verify(mapper, never()).toDto(costeo2);
    }

    // --- obtenerResumenEVN ---

    @Test
    @DisplayName("obtenerResumenEVN retorna tela, composicion, genero y modelo cuando hay item TELAS con Articulo")
    void obtenerResumenEVN_conTelaYArticulo_retornaResumenCompleto() {
        CosteoItem itemTela = CosteoItem.builder().tipoInsumo("TELAS").articuloId(50).nombreInsumo("Tela Cruda")
                .build();
        Costeo domain = costeo(1L, 5L, List.of(itemTela));
        domain.setNumeroCosteo(new DocumentNumber("COST-001"));

        Articulo articulo = articuloConTela(50, "Algodón Pima", "100% Algodón");
        SolicitudCostos scos = solicitudCostos(5L, 100L, "Cliente X", 200L, "Vendedor Y", "Hombre", "Polera Cuello V");

        when(repository.findById(1L)).thenReturn(Optional.of(domain));
        when(articuloRepository.findById(50)).thenReturn(Optional.of(articulo));
        when(scosRepository.findById(5L)).thenReturn(Optional.of(scos));

        Optional<CosteoResumenEVNDTO> resultado = costeoService.obtenerResumenEVN(1L);

        assertThat(resultado).isPresent();
        CosteoResumenEVNDTO resumen = resultado.get();
        assertThat(resumen.getIdCosteo()).isEqualTo(1L);
        assertThat(resumen.getNumeroCosteo()).isEqualTo("COST-001");
        assertThat(resumen.getSolicitudCostosId()).isEqualTo(5L);
        assertThat(resumen.getTela()).isEqualTo("Algodón Pima");
        assertThat(resumen.getComposicion()).isEqualTo("100% Algodón");
        assertThat(resumen.getGenero()).isEqualTo("Hombre");
        assertThat(resumen.getModelo()).isEqualTo("Polera Cuello V");
    }

    @Test
    @DisplayName("obtenerResumenEVN usa el nombreInsumo cuando el item TELAS no tiene Articulo asociado")
    void obtenerResumenEVN_itemTelaSinArticulo_usaNombreInsumo() {
        CosteoItem itemTela = CosteoItem.builder().tipoInsumo("TELAS").articuloId(null).nombreInsumo("Tela Genérica")
                .build();
        Costeo domain = costeo(1L, null, List.of(itemTela));

        when(repository.findById(1L)).thenReturn(Optional.of(domain));

        Optional<CosteoResumenEVNDTO> resultado = costeoService.obtenerResumenEVN(1L);

        assertThat(resultado).isPresent();
        CosteoResumenEVNDTO resumen = resultado.get();
        assertThat(resumen.getTela()).isEqualTo("Tela Genérica");
        assertThat(resumen.getComposicion()).isNull();
        verify(articuloRepository, never()).findById(any());
    }

    @Test
    @DisplayName("obtenerResumenEVN sin items TELAS ni solicitud de costos retorna campos nulos")
    void obtenerResumenEVN_sinItemTelaNiSolicitud_retornaCamposNulos() {
        Costeo domain = costeo(1L, null, new ArrayList<>());

        when(repository.findById(1L)).thenReturn(Optional.of(domain));

        Optional<CosteoResumenEVNDTO> resultado = costeoService.obtenerResumenEVN(1L);

        assertThat(resultado).isPresent();
        CosteoResumenEVNDTO resumen = resultado.get();
        assertThat(resumen.getTela()).isNull();
        assertThat(resumen.getComposicion()).isNull();
        assertThat(resumen.getGenero()).isNull();
        assertThat(resumen.getModelo()).isNull();
        verify(scosRepository, never()).findById(any());
    }

    @Test
    @DisplayName("obtenerResumenEVN retorna Optional vacio si el costeo no existe")
    void obtenerResumenEVN_noExistente_retornaOptionalEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<CosteoResumenEVNDTO> resultado = costeoService.obtenerResumenEVN(99L);

        assertThat(resultado).isEmpty();
    }

    // --- Transiciones de estado ---

    @Test
    @DisplayName("rechazar versiona el costeo, fija RECHAZADO y conserva el motivo")
    void rechazar_versionaYRechaza() {
        Costeo domain = costeo(1L, null, new ArrayList<>());
        domain.marcarCosteado(); // BORRADOR → COSTEADO

        when(repository.findById(1L)).thenReturn(Optional.of(domain));
        when(repository.save(any(Costeo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(Costeo.class))).thenReturn(CosteoDTO.builder().idCosteo(1L).build());

        costeoService.rechazar(1L, "Diferencias detectadas", "Ana", "JEFE_PRODUCCION");

        verify(crearVersionCosteoUseCase).ejecutar(1L, "Rechazo v1: Diferencias detectadas", "Ana");
        ArgumentCaptor<Costeo> captor = ArgumentCaptor.forClass(Costeo.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getEstado())
                .isEqualTo(backend.com.produccion.domain.enums.EstadoCosteo.RECHAZADO);
        assertThat(captor.getValue().getMotivoRechazo()).isEqualTo("Diferencias detectadas");
        // Se registra el historial de estado con la versión y el actor real.
        verify(historialEstadoService).registrar(eq("COSTEO"), eq(1L), eq("COSTEADO"), eq("RECHAZADO"),
                eq("Ana"), org.mockito.ArgumentMatchers.contains("v1"));
    }

    @Test
    @DisplayName("aprobar/rechazar con rol no autorizado lanza ForbiddenException (403)")
    void decision_rolNoAutorizado() {
        assertThatThrownBy(() -> costeoService.aprobar(1L, "Pedro", "VENDEDOR"))
                .isInstanceOf(backend.com.shared.exception.ForbiddenException.class);
        assertThatThrownBy(() -> costeoService.rechazar(1L, "x", "Pedro", "OPERARIO_PRODUCCION"))
                .isInstanceOf(backend.com.shared.exception.ForbiddenException.class);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("aprobar sin firma del actor lanza ValidationException")
    void aprobar_sinActor() {
        assertThatThrownBy(() -> costeoService.aprobar(1L, "  ", "JEFE_PRODUCCION"))
                .isInstanceOf(backend.com.shared.exception.ValidationException.class);
    }

    @Test
    @DisplayName("reabrir (retomar) desde RECHAZADO incrementa la versión y registra historial")
    void reabrir_retomarIncrementaVersion() {
        Costeo domain = costeo(1L, null, new ArrayList<>());
        domain.marcarCosteado();
        domain.rechazar("Diferencias"); // RECHAZADO, version 1

        when(repository.findById(1L)).thenReturn(Optional.of(domain));
        when(repository.save(any(Costeo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(Costeo.class))).thenReturn(CosteoDTO.builder().idCosteo(1L).build());

        costeoService.reabrir(1L);

        ArgumentCaptor<Costeo> captor = ArgumentCaptor.forClass(Costeo.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getEstado())
                .isEqualTo(backend.com.produccion.domain.enums.EstadoCosteo.BORRADOR);
        assertThat(captor.getValue().getVersion()).isEqualTo(2);
        verify(historialEstadoService).registrar(eq("COSTEO"), eq(1L), eq("RECHAZADO"), eq("BORRADOR"),
                eq("SYSTEM"), org.mockito.ArgumentMatchers.contains("v2"));
    }

    @Test
    @DisplayName("aprobar lleva COSTEADO → APROBADO")
    void aprobar_ok() {
        Costeo domain = costeo(1L, null, new ArrayList<>());
        domain.marcarCosteado();

        when(repository.findById(1L)).thenReturn(Optional.of(domain));
        when(repository.save(any(Costeo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(Costeo.class))).thenReturn(CosteoDTO.builder().idCosteo(1L).build());

        costeoService.aprobar(1L, "Ana", "JEFE_PRODUCCION");

        ArgumentCaptor<Costeo> captor = ArgumentCaptor.forClass(Costeo.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getEstado())
                .isEqualTo(backend.com.produccion.domain.enums.EstadoCosteo.APROBADO);
        verify(crearVersionCosteoUseCase, never()).ejecutar(any(), any(), any());
        verify(historialEstadoService).registrar(eq("COSTEO"), eq(1L), eq("COSTEADO"), eq("APROBADO"),
                eq("Ana"), org.mockito.ArgumentMatchers.contains("aprobado"));
    }

    @Test
    @DisplayName("transición sobre costeo inexistente lanza EntityNotFoundException")
    void transicion_costeoInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> costeoService.aprobar(99L, "Ana", "JEFE_PRODUCCION"))
                .isInstanceOf(backend.com.shared.exception.EntityNotFoundException.class);
    }
}
