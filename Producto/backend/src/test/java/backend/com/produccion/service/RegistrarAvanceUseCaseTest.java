package backend.com.produccion.service;

import backend.com.produccion.application.UseCase.RegistrarAvanceUseCase;
import backend.com.produccion.application.dto.RegistrarAvanceCommand;
import backend.com.produccion.application.dto.RegistroAvanceDTO;
import backend.com.produccion.domain.enums.FaseProduccion;
import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.domain.model.RegistroAvance;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import backend.com.produccion.domain.repository.RegistroAvanceRepository;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests del UseCase con dependencias mockeadas (Mockito).
 * Validan reglas de orquestación sin tocar Spring ni la base de datos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarAvanceUseCase")
class RegistrarAvanceUseCaseTest {

    @Mock
    private OrdenTrabajoRepository otRepository;
    @Mock
    private RegistroAvanceRepository avanceRepository;
    @Mock
    private HistorialEstadoService historialService;

    @InjectMocks
    private RegistrarAvanceUseCase useCase;

    private OrdenTrabajo otBase;

    @BeforeEach
    void setUp() {
        otBase = OrdenTrabajo.crearParaFase(
                new DocumentNumber(1L), 1L, 1L, 1L, 1, FaseProduccion.CORTE, 100, null);
    }

    private RegistrarAvanceCommand cmd(Integer prod, Integer merma, String motivo, String usuario) {
        RegistrarAvanceCommand c = new RegistrarAvanceCommand();
        c.setCantidadProducida(prod);
        c.setCantidadMerma(merma);
        c.setMotivoMerma(motivo);
        c.setUsuario(usuario);
        return c;
    }

    @Test
    @DisplayName("registra avance, actualiza la OT y persiste el registro")
    void registraAvance() {
        when(otRepository.findById(1L)).thenReturn(Optional.of(otBase));
        when(otRepository.save(any(OrdenTrabajo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(avanceRepository.save(any(RegistroAvance.class)))
                .thenAnswer(inv -> {
                    RegistroAvance r = inv.getArgument(0);
                    return new RegistroAvance(99L, r.getOrdenTrabajoId(), r.getCantidadProducida(),
                            r.getCantidadMerma(), r.getMotivoMerma(), r.getUsuario(),
                            r.getObservacion(), LocalDateTime.now());
                });

        RegistroAvanceDTO dto = useCase.registrar(1L, cmd(30, 0, null, "operario1"));

        assertThat(dto.getCantidadProducida()).isEqualTo(30);
        assertThat(dto.getUsuario()).isEqualTo("operario1");
        verify(otRepository).save(any(OrdenTrabajo.class));
        verify(avanceRepository).save(any(RegistroAvance.class));
    }

    @Test
    @DisplayName("registra cambio de estado en historial al transicionar PENDIENTE → EN_PROCESO")
    void registraHistorialAlIniciar() {
        when(otRepository.findById(1L)).thenReturn(Optional.of(otBase));
        when(otRepository.save(any(OrdenTrabajo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(avanceRepository.save(any(RegistroAvance.class)))
                .thenAnswer(inv -> {
                    RegistroAvance r = inv.getArgument(0);
                    return new RegistroAvance(99L, r.getOrdenTrabajoId(), r.getCantidadProducida(),
                            r.getCantidadMerma(), r.getMotivoMerma(), r.getUsuario(),
                            r.getObservacion(), LocalDateTime.now());
                });

        useCase.registrar(1L, cmd(10, 0, null, "operario1"));

        verify(historialService).registrar(
                org.mockito.ArgumentMatchers.eq("OT"),
                any(),
                org.mockito.ArgumentMatchers.eq("PENDIENTE"),
                org.mockito.ArgumentMatchers.eq("EN_PROCESO"),
                org.mockito.ArgumentMatchers.eq("operario1"),
                org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("falla si el comando es nulo")
    void fallaConComandoNulo() {
        assertThatThrownBy(() -> useCase.registrar(1L, null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("falla si no se registra ninguna unidad")
    void fallaSinUnidades() {
        assertThatThrownBy(() -> useCase.registrar(1L, cmd(0, 0, null, "u")))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("al menos");
        verify(otRepository, never()).save(any());
    }

    @Test
    @DisplayName("falla si hay mermas sin motivo")
    void fallaMermaSinMotivo() {
        assertThatThrownBy(() -> useCase.registrar(1L, cmd(0, 5, null, "u")))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("motivo");
    }

    @Test
    @DisplayName("falla si no se identifica al usuario")
    void fallaSinUsuario() {
        assertThatThrownBy(() -> useCase.registrar(1L, cmd(10, 0, null, "  ")))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("usuario");
    }

    @Test
    @DisplayName("falla con EntityNotFoundException si la OT no existe")
    void fallaSiOtNoExiste() {
        when(otRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.registrar(99L, cmd(10, 0, null, "u")))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("falla si el avance excedería la cantidad total")
    void fallaSiExcedeTotal() {
        when(otRepository.findById(1L)).thenReturn(Optional.of(otBase));

        assertThatThrownBy(() -> useCase.registrar(1L, cmd(150, 0, null, "u")))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("excede");
    }
}
