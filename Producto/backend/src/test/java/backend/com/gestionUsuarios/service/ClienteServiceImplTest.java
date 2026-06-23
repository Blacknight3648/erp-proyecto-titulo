package backend.com.gestionUsuarios.service;

import backend.com.comercial.infrastructure.persistence.repository.EvaluacionNegocioJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.NotaVentaJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCostosJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCotizacionJpaRepository;
import backend.com.gestionUsuarios.application.service.ClienteValidator;
import backend.com.gestionUsuarios.application.service.impl.ClienteServiceImpl;
import backend.com.gestionUsuarios.domain.model.Cliente;
import backend.com.gestionUsuarios.domain.repository.ClienteRepository;
import backend.com.gestionUsuarios.infrastructure.exception.ClienteNotFoundException;
import backend.com.shared.domain.model.Giro;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import backend.com.shared.infrastructure.persistence.repository.GiroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - ClienteServiceImpl")
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private GiroRepository giroRepository;
    @Mock
    private GiroMapper giroMapper;
    @Mock
    private ClienteValidator clienteValidator;
    @Mock
    private EvaluacionNegocioJpaRepository evaluacionNegocioRepository;
    @Mock
    private NotaVentaJpaRepository notaVentaRepository;
    @Mock
    private SolicitudCostosJpaRepository solicitudCostosRepository;
    @Mock
    private SolicitudCotizacionJpaRepository solicitudCotizacionRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente crearClienteMock(Long id, String run) {
        Cliente cliente = new Cliente();
        cliente.setClienteId(id);
        cliente.setRunCliente(run);
        cliente.setRazonSocial("Empresa Test");
        cliente.setActivo(true);
        return cliente;
    }

    @Nested
    @DisplayName("Consultas de Cliente")
    class ConsultasTests {

        @Test
        @DisplayName("Debe listar todos los clientes")
        void listarTodos() {
            when(clienteRepository.findAll()).thenReturn(List.of(crearClienteMock(1L, "123-4")));
            List<Cliente> result = clienteService.listarTodos();
            assertEquals(1, result.size());
            verify(clienteRepository).findAll();
        }

        @Test
        @DisplayName("Debe obtener cliente por ID")
        void obtenerPorId() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(crearClienteMock(1L, "123-4")));
            Optional<Cliente> result = clienteService.obtenerPorId(1L);
            assertTrue(result.isPresent());
            assertEquals("123-4", result.get().getRunCliente());
        }

        @Test
        @DisplayName("Debe obtener cliente por RUN")
        void obtenerPorRun() {
            when(clienteRepository.findByRunCliente("123-4")).thenReturn(Optional.of(crearClienteMock(1L, "123-4")));
            Optional<Cliente> result = clienteService.obtenerPorRun("123-4");
            assertTrue(result.isPresent());
        }
    }

    @Nested
    @DisplayName("Mutaciones de Cliente")
    class MutacionesTests {

        @Test
        @DisplayName("Debe crear cliente exitosamente sin giro")
        void crear_Exito() {
            Cliente input = crearClienteMock(null, "123-4");
            Cliente saved = crearClienteMock(1L, "123-4");

            doNothing().when(clienteValidator).validateUniqueness("123-4");
            when(clienteRepository.save(any(Cliente.class))).thenReturn(saved);

            Cliente result = clienteService.crear(input);

            assertNotNull(result.getClienteId());
            verify(clienteRepository).save(input);
            verify(clienteValidator).validateUniqueness("123-4");
        }

        @Test
        @DisplayName("Debe actualizar cliente exitosamente")
        void actualizar_Exito() {
            Cliente existente = crearClienteMock(1L, "123-4");
            Cliente input = crearClienteMock(null, "123-4");
            input.setRazonSocial("Empresa Modificada");

            when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(clienteRepository.save(any(Cliente.class))).thenReturn(existente);

            Cliente result = clienteService.actualizar(1L, input);

            assertEquals("Empresa Modificada", result.getRazonSocial());
            verify(clienteRepository).save(existente);
        }

        @Test
        @DisplayName("Debe lanzar ClienteNotFoundException al actualizar si no existe")
        void actualizar_NoExiste_LanzaException() {
            when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ClienteNotFoundException.class, () -> clienteService.actualizar(99L, new Cliente()));
        }

        @Test
        @DisplayName("Debe eliminar cliente exitosamente y sus dependencias")
        void eliminar_Exito() {
            when(clienteRepository.existsById(1L)).thenReturn(true);

            assertDoesNotThrow(() -> clienteService.eliminar(1L));

            verify(notaVentaRepository).deleteByCliente_ClienteId(1L);
            verify(evaluacionNegocioRepository).deleteByCliente_ClienteId(1L);
            verify(solicitudCostosRepository).deleteByCliente_ClienteId(1L);
            verify(solicitudCotizacionRepository).deleteByCliente_ClienteId(1L);
            verify(clienteRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Debe lanzar ClienteNotFoundException al eliminar si no existe")
        void eliminar_NoExiste_LanzaException() {
            when(clienteRepository.existsById(99L)).thenReturn(false);

            assertThrows(ClienteNotFoundException.class, () -> clienteService.eliminar(99L));
            verify(clienteRepository, never()).deleteById(any());
        }
    }
}
