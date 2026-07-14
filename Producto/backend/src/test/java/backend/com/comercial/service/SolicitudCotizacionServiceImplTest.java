package backend.com.comercial.service;

import backend.com.comercial.application.dto.SCOSLogotipoDTO;
import backend.com.comercial.application.dto.SCOTPrendaListaDTO;
import backend.com.comercial.application.dto.SolicitudCotizacionesCreateDTO;
import backend.com.comercial.application.dto.SolicitudCotizacionesDTO;
import backend.com.comercial.application.service.impl.SolicitudCotizacionServiceImpl;
import backend.com.comercial.domain.model.SolicitudCotizacion;
import backend.com.comercial.domain.repository.SolicitudCotizacionRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SolicitudCotizacionServiceImpl")
class SolicitudCotizacionServiceImplTest {

    @Mock
    private SolicitudCotizacionRepository repository;

    @Mock
    private backend.com.shared.application.service.NumeroDocumentoService numeroDocumentoService;

    @InjectMocks
    private SolicitudCotizacionServiceImpl solicitudCotizacionServiceImpl;

    // ---------------- HELPERS ----------------

    private SolicitudCotizacion solicitudCotizacion(Long id, String numero, String estado, Integer cantidad) {
        return new SolicitudCotizacion(id, new DocumentNumber(numero), estado, "SCOT", 1L, 2L, null,
                "Polera basica", false, false, cantidad, LocalDate.now(), new ArrayList<>(), new ArrayList<>());
    }

    /** Simula la persistencia: asigna idSCOT a la entidad recibida, preservando el resto de los datos. */
    private SolicitudCotizacion persistido(SolicitudCotizacion d, Long idAsignado) {
        return new SolicitudCotizacion(idAsignado, d.getNumeroSCOT(), d.getEstado(), d.getTipo(), d.getClienteId(),
                d.getVendedorId(), d.getEspecificacionTecnicaId(), d.getArticuloDescripcion(), d.getEsMuestra(),
                d.getHasLogo(), d.getCantidad(), d.getFecha(), d.getPrendas(), d.getLogotipos());
    }

    // ---------------- create ----------------

    @Test
    @DisplayName("create genera el número correlativo y persiste prendas y logotipos")
    void create_ok() {
        SCOTPrendaListaDTO prendaDto = new SCOTPrendaListaDTO("Polera", 5, "M", "Rojo", "PROV-1",
                "http://link", "Algodon", "0.2", "obs", 1000.0, null);

        SCOSLogotipoDTO logotipoDto = SCOSLogotipoDTO.builder()
                .tipo("Bordado")
                .nombre("Logo")
                .ubicacion("Pecho")
                .color("Negro")
                .tamanio("5.0 cm")
                .cantidad(2)
                .precio(BigDecimal.valueOf(1000))
                .build();

        SolicitudCotizacionesCreateDTO dto = SolicitudCotizacionesCreateDTO.builder()
                .clienteId(1L)
                .vendedorId(2L)
                .articuloDescripcion("Polera basica")
                .cantidad(5)
                .tipo("SCOT")
                .prendas(List.of(prendaDto))
                .logotipos(List.of(logotipoDto))
                .build();

        when(numeroDocumentoService.siguienteFormateado("SCOT")).thenReturn(new DocumentNumber("SCOT-0000003"));
        when(repository.save(any(SolicitudCotizacion.class)))
                .thenAnswer(invocation -> persistido(invocation.getArgument(0), 1L));

        SolicitudCotizacionesDTO resultado = solicitudCotizacionServiceImpl.create(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNumero()).isEqualTo("SCOT-0000003");
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        assertThat(resultado.getMoneda()).isEqualTo("CLP");
        assertThat(resultado.getEsMuestra()).isFalse();
        assertThat(resultado.getHasLogo()).isFalse();

        assertThat(resultado.getPrendas()).hasSize(1);
        SCOTPrendaListaDTO prendaResultado = resultado.getPrendas().get(0);
        assertThat(prendaResultado.getNombre()).isEqualTo("Polera");
        assertThat(prendaResultado.getPrecioUnitario()).isEqualTo(1000.0);
        assertThat(prendaResultado.getCostoTotal()).isEqualTo(5000.0);

        assertThat(resultado.getLogotipos()).hasSize(1);
        assertThat(resultado.getLogotipos().get(0).getNombre()).isEqualTo("Logo");
    }

    @Test
    @DisplayName("create asigna PENDIENTE y deja prendas/logotipos vacíos cuando no se envían")
    void create_sinPrendasNiLogotipos() {
        SolicitudCotizacionesCreateDTO dto = SolicitudCotizacionesCreateDTO.builder()
                .clienteId(1L)
                .articuloDescripcion("Polera basica")
                .cantidad(5)
                .build();

        when(numeroDocumentoService.siguienteFormateado("SCOT")).thenReturn(new DocumentNumber("SCOT-0000001"));
        when(repository.save(any(SolicitudCotizacion.class)))
                .thenAnswer(invocation -> persistido(invocation.getArgument(0), 1L));

        SolicitudCotizacionesDTO resultado = solicitudCotizacionServiceImpl.create(dto);

        assertThat(resultado.getNumero()).isEqualTo("SCOT-0000001");
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        assertThat(resultado.getPrendas()).isEmpty();
        assertThat(resultado.getLogotipos()).isEmpty();
    }

    // ---------------- update ----------------

    @Test
    @DisplayName("update actualiza los datos y reemplaza prendas y logotipos")
    void update_ok() {
        SolicitudCotizacion existente = solicitudCotizacion(1L, "SCOT-0001", "PENDIENTE", 5);

        SCOTPrendaListaDTO prendaDto = new SCOTPrendaListaDTO("Polera nueva", 10, "L", "Azul", "PROV-2",
                "http://link2", "Poliester", "0.3", "obs2", 2000.0, null);

        SolicitudCotizacionesCreateDTO dto = SolicitudCotizacionesCreateDTO.builder()
                .estado("APROBADA")
                .articuloDescripcion("Polera actualizada")
                .cantidad(10)
                .prendas(List.of(prendaDto))
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(SolicitudCotizacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SolicitudCotizacionesDTO resultado = solicitudCotizacionServiceImpl.update(1L, dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo("APROBADA");
        assertThat(resultado.getArticuloDescripcion()).isEqualTo("Polera actualizada");
        assertThat(resultado.getCantidad()).isEqualTo(10);
        assertThat(resultado.getPrendas()).hasSize(1);
        assertThat(resultado.getPrendas().get(0).getNombre()).isEqualTo("Polera nueva");
    }

    @Test
    @DisplayName("update conserva los valores existentes cuando el DTO no envía cambios")
    void update_conservaValoresExistentes() {
        SolicitudCotizacion existente = solicitudCotizacion(1L, "SCOT-0001", "PENDIENTE", 5);

        SolicitudCotizacionesCreateDTO dto = SolicitudCotizacionesCreateDTO.builder().build();

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(SolicitudCotizacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SolicitudCotizacionesDTO resultado = solicitudCotizacionServiceImpl.update(1L, dto);

        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        assertThat(resultado.getArticuloDescripcion()).isEqualTo("Polera basica");
        assertThat(resultado.getCantidad()).isEqualTo(5);
    }

    @Test
    @DisplayName("update lanza excepción si la cotización no existe")
    void update_noExiste() {
        SolicitudCotizacionesCreateDTO dto = SolicitudCotizacionesCreateDTO.builder().build();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitudCotizacionServiceImpl.update(1L, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(repository, never()).save(any());
    }

    // ---------------- findById ----------------

    @Test
    @DisplayName("findById retorna el DTO mapeado cuando la cotización existe")
    void findById_ok() {
        SolicitudCotizacion existente = solicitudCotizacion(1L, "SCOT-0001", "PENDIENTE", 5);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        Optional<SolicitudCotizacionesDTO> resultado = solicitudCotizacionServiceImpl.findById(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
        assertThat(resultado.get().getNumero()).isEqualTo("SCOT-0001");
    }

    @Test
    @DisplayName("findById retorna Optional vacío cuando la cotización no existe")
    void findById_noExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<SolicitudCotizacionesDTO> resultado = solicitudCotizacionServiceImpl.findById(1L);

        assertThat(resultado).isEmpty();
    }

    // ---------------- findAll ----------------

    @Test
    @DisplayName("findAll retorna el mapeo de todas las cotizaciones")
    void findAll_ok() {
        SolicitudCotizacion s1 = solicitudCotizacion(1L, "SCOT-0001", "PENDIENTE", 5);
        SolicitudCotizacion s2 = solicitudCotizacion(2L, "SCOT-0002", "APROBADA", 10);

        when(repository.findAll()).thenReturn(List.of(s1, s2));

        List<SolicitudCotizacionesDTO> resultado = solicitudCotizacionServiceImpl.findAll();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNumero()).isEqualTo("SCOT-0001");
        assertThat(resultado.get(1).getNumero()).isEqualTo("SCOT-0002");
    }

    // ---------------- delete ----------------

    @Test
    @DisplayName("delete elimina la cotización por id")
    void delete_ok() {
        solicitudCotizacionServiceImpl.delete(1L);

        verify(repository).deleteById(1L);
    }
}
