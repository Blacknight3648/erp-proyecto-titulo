package backend.com.shared.service;

import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.domain.model.HistorialEstado;
import backend.com.shared.infrastructure.persistence.repository.HistorialEstadoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - HistorialEstadoService")
class HistorialEstadoServiceTest {

    @Mock
    private HistorialEstadoRepository repository;

    @InjectMocks
    private HistorialEstadoService service;

    @Test
    @DisplayName("Debe registrar un nuevo historial de estado")
    void registrar() {
        String tipoEntidad = "NOTA_VENTA";
        Long entidadId = 1L;
        String estadoAnterior = "BORRADOR";
        String estadoNuevo = "ENVIADO";
        String usuario = "jperez";
        String observacion = "Cambio de estado manual";

        service.registrar(tipoEntidad, entidadId, estadoAnterior, estadoNuevo, usuario, observacion);

        ArgumentCaptor<HistorialEstado> captor = ArgumentCaptor.forClass(HistorialEstado.class);
        verify(repository).save(captor.capture());

        HistorialEstado guardado = captor.getValue();
        assertEquals(tipoEntidad, guardado.getTipoEntidad());
        assertEquals(entidadId, guardado.getEntidadId());
        assertEquals(estadoAnterior, guardado.getEstadoAnterior());
        assertEquals(estadoNuevo, guardado.getEstadoNuevo());
        assertEquals(usuario, guardado.getUsuario());
        assertEquals(observacion, guardado.getObservacion());
        assertNotNull(guardado.getFecha());
    }

    @Test
    @DisplayName("Debe consultar y mapear historiales de estado exitosamente")
    void consultar() {
        String tipoEntidad = "NOTA_VENTA";
        Long entidadId = 1L;

        HistorialEstado historial = new HistorialEstado(
                10L, tipoEntidad, entidadId, "BORRADOR", "ENVIADO",
                "jperez", "Cambio de estado manual", LocalDateTime.now()
        );

        when(repository.findByEntidad(tipoEntidad, entidadId)).thenReturn(List.of(historial));

        List<HistorialEstadoDTO> resultados = service.consultar(tipoEntidad, entidadId);

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals(historial.getId(), resultados.get(0).getId());
        assertEquals(historial.getEstadoAnterior(), resultados.get(0).getEstadoAnterior());
        assertEquals(historial.getEstadoNuevo(), resultados.get(0).getEstadoNuevo());
        assertEquals(historial.getObservacion(), resultados.get(0).getObservacion());
    }
}
