package backend.com.shared.service;

import backend.com.shared.application.service.impl.GiroServiceImpl;
import backend.com.shared.domain.model.Giro;
import backend.com.shared.domain.model.Rubro;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.GiroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GiroServiceImpl")
class GiroServiceImplTest {

    @Mock
    private GiroRepository giroRepository;

    @Mock
    private GiroMapper giroMapper;

    @InjectMocks
    private GiroServiceImpl giroServiceImpl;

    // ---------------- HELPERS ----------------

    private GiroJpaEntity entity(Long id, String codigoSii, String nombreGiro, String descripcionGiro) {
        return GiroJpaEntity.builder()
                .giroId(id)
                .codigoSii(codigoSii)
                .nombreGiro(nombreGiro)
                .descripcionGiro(descripcionGiro)
                .build();
    }

    private Giro giro(Long id, String codigoSii, String nombreGiro, String descripcionGiro, Rubro rubro) {
        return Giro.builder()
                .giroId(id)
                .codigoSii(codigoSii)
                .nombreGiro(nombreGiro)
                .descripcionGiro(descripcionGiro)
                .rubro(rubro)
                .build();
    }

    private Rubro rubro(Long id, String nombre, String descripcion) {
        return Rubro.builder()
                .rubroId(id)
                .nombreRubro(nombre)
                .descripcionRubro(descripcion)
                .build();
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        GiroJpaEntity e1 = entity(1L, "111", "Comercio", "Venta de productos");
        GiroJpaEntity e2 = entity(2L, "222", "Servicios", "Prestación de servicios");
        Giro g1 = giro(1L, "111", "Comercio", "Venta de productos", null);
        Giro g2 = giro(2L, "222", "Servicios", "Prestación de servicios", null);

        when(giroRepository.findAll()).thenReturn(List.of(e1, e2));
        when(giroMapper.toDomain(e1)).thenReturn(g1);
        when(giroMapper.toDomain(e2)).thenReturn(g2);

        List<Giro> resultado = giroServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(g1, g2);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el giro cuando existe")
    void obtenerPorId_ok() {
        GiroJpaEntity e1 = entity(1L, "111", "Comercio", "Venta de productos");
        Giro g1 = giro(1L, "111", "Comercio", "Venta de productos", null);

        when(giroRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(giroMapper.toDomain(e1)).thenReturn(g1);

        Optional<Giro> resultado = giroServiceImpl.obtenerPorId(1L);

        assertThat(resultado).contains(g1);
    }

    @Test
    @DisplayName("obtenerPorId retorna vacío si el giro no existe")
    void obtenerPorId_noExiste() {
        when(giroRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Giro> resultado = giroServiceImpl.obtenerPorId(1L);

        assertThat(resultado).isEmpty();
    }

    // ---------------- obtenerPorCodigoSii ----------------

    @Test
    @DisplayName("obtenerPorCodigoSii retorna el giro cuando existe")
    void obtenerPorCodigoSii_ok() {
        GiroJpaEntity e1 = entity(1L, "111", "Comercio", "Venta de productos");
        Giro g1 = giro(1L, "111", "Comercio", "Venta de productos", null);

        when(giroRepository.findByCodigoSii("111")).thenReturn(Optional.of(e1));
        when(giroMapper.toDomain(e1)).thenReturn(g1);

        Optional<Giro> resultado = giroServiceImpl.obtenerPorCodigoSii("111");

        assertThat(resultado).contains(g1);
    }

    @Test
    @DisplayName("obtenerPorCodigoSii retorna vacío si no existe")
    void obtenerPorCodigoSii_noExiste() {
        when(giroRepository.findByCodigoSii("999")).thenReturn(Optional.empty());

        Optional<Giro> resultado = giroServiceImpl.obtenerPorCodigoSii("999");

        assertThat(resultado).isEmpty();
    }

    // ---------------- buscarPorNombreGiro ----------------

    @Test
    @DisplayName("buscarPorNombreGiro mapea correctamente los resultados")
    void buscarPorNombreGiro_ok() {
        GiroJpaEntity e1 = entity(1L, "111", "Comercio", "Venta de productos");
        Giro g1 = giro(1L, "111", "Comercio", "Venta de productos", null);

        when(giroRepository.findByNombreGiroContainingIgnoreCase("comer")).thenReturn(List.of(e1));
        when(giroMapper.toDomain(e1)).thenReturn(g1);

        List<Giro> resultado = giroServiceImpl.buscarPorNombreGiro("comer");

        assertThat(resultado).containsExactly(g1);
    }

    // ---------------- obtenerPorDescripcionGiro ----------------

    @Test
    @DisplayName("obtenerPorDescripcionGiro mapea correctamente los resultados")
    void obtenerPorDescripcionGiro_ok() {
        GiroJpaEntity e1 = entity(1L, "111", "Comercio", "Venta de productos");
        Giro g1 = giro(1L, "111", "Comercio", "Venta de productos", null);

        when(giroRepository.findByDescripcionGiroContainingIgnoreCase("venta")).thenReturn(List.of(e1));
        when(giroMapper.toDomain(e1)).thenReturn(g1);

        List<Giro> resultado = giroServiceImpl.obtenerPorDescripcionGiro("venta");

        assertThat(resultado).containsExactly(g1);
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente y limpia el id antes de persistir")
    void crear_ok() {
        Giro nuevo = giro(null, "111", "Comercio", "Venta de productos", null);
        GiroJpaEntity entidadMapeada = entity(5L, "111", "Comercio", "Venta de productos");
        GiroJpaEntity guardado = entity(1L, "111", "Comercio", "Venta de productos");
        Giro esperado = giro(1L, "111", "Comercio", "Venta de productos", null);

        when(giroRepository.existsByCodigoSii("111")).thenReturn(false);
        when(giroMapper.toEntity(nuevo)).thenReturn(entidadMapeada);
        when(giroRepository.save(entidadMapeada)).thenReturn(guardado);
        when(giroMapper.toDomain(guardado)).thenReturn(esperado);

        Giro resultado = giroServiceImpl.crear(nuevo);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(entidadMapeada.getGiroId()).isNull();
    }

    @Test
    @DisplayName("crear no valida duplicados si el código SII es nulo")
    void crear_sinCodigoSii() {
        Giro nuevo = giro(null, null, "Comercio", "Venta de productos", null);
        GiroJpaEntity entidadMapeada = entity(null, null, "Comercio", "Venta de productos");
        GiroJpaEntity guardado = entity(1L, null, "Comercio", "Venta de productos");
        Giro esperado = giro(1L, null, "Comercio", "Venta de productos", null);

        when(giroMapper.toEntity(nuevo)).thenReturn(entidadMapeada);
        when(giroRepository.save(entidadMapeada)).thenReturn(guardado);
        when(giroMapper.toDomain(guardado)).thenReturn(esperado);

        Giro resultado = giroServiceImpl.crear(nuevo);

        assertThat(resultado).isEqualTo(esperado);
        verify(giroRepository, never()).existsByCodigoSii(any());
    }

    @Test
    @DisplayName("crear lanza excepción si el código SII ya existe")
    void crear_duplicado() {
        Giro nuevo = giro(null, "111", "Comercio", "Venta de productos", null);

        when(giroRepository.existsByCodigoSii("111")).thenReturn(true);

        assertThatThrownBy(() -> giroServiceImpl.crear(nuevo))
                .isInstanceOf(BusinessRuleException.class);

        verify(giroRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica los datos cuando el giro existe")
    void actualizar_ok() {
        GiroJpaEntity existente = entity(1L, "111", "Comercio", "Venta de productos");
        Rubro rubroDomain = rubro(2L, "Textil", "Rubro textil");
        Giro actualizado = giro(null, "222", "Servicios", "Prestación de servicios", rubroDomain);
        GiroJpaEntity guardado = entity(1L, "222", "Servicios", "Prestación de servicios");
        Giro esperado = giro(1L, "222", "Servicios", "Prestación de servicios", rubroDomain);

        when(giroRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(giroRepository.save(existente)).thenReturn(guardado);
        when(giroMapper.toDomain(guardado)).thenReturn(esperado);

        Giro resultado = giroServiceImpl.actualizar(1L, actualizado);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getCodigoSii()).isEqualTo("222");
        assertThat(existente.getNombreGiro()).isEqualTo("Servicios");
        assertThat(existente.getDescripcionGiro()).isEqualTo("Prestación de servicios");
        assertThat(existente.getRubro().getRubroId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("actualizar lanza excepción si el giro no existe")
    void actualizar_noExiste() {
        Rubro rubroDomain = rubro(2L, "Textil", "Rubro textil");
        Giro actualizado = giro(null, "222", "Servicios", "Prestación de servicios", rubroDomain);

        when(giroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> giroServiceImpl.actualizar(1L, actualizado))
                .isInstanceOf(EntityNotFoundException.class);

        verify(giroRepository, never()).save(any());
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando el giro existe")
    void eliminar_ok() {
        when(giroRepository.existsById(1L)).thenReturn(true);

        giroServiceImpl.eliminar(1L);

        verify(giroRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar lanza excepción si el giro no existe")
    void eliminar_noExiste() {
        when(giroRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> giroServiceImpl.eliminar(1L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(giroRepository, never()).deleteById(any());
    }

    // ---------------- obtenerOCrearPorNombreGiro ----------------

    @Test
    @DisplayName("obtenerOCrearPorNombreGiro retorna vacío si el nombre está en blanco")
    void obtenerOCrear_blanco() {
        Optional<Giro> resultado = giroServiceImpl.obtenerOCrearPorNombreGiro("   ");

        assertThat(resultado).isEmpty();
        verifyNoInteractions(giroRepository);
    }

    @Test
    @DisplayName("obtenerOCrearPorNombreGiro retorna el existente cuando ya está registrado")
    void obtenerOCrear_existente() {
        GiroJpaEntity existente = entity(1L, null, "Comercio", null);
        Giro esperado = giro(1L, null, "Comercio", null, null);

        when(giroRepository.findByNombreGiroIgnoreCase("Comercio")).thenReturn(Optional.of(existente));
        when(giroMapper.toDomain(existente)).thenReturn(esperado);

        Optional<Giro> resultado = giroServiceImpl.obtenerOCrearPorNombreGiro(" Comercio ");

        assertThat(resultado).contains(esperado);
        verify(giroRepository, never()).save(any());
    }

    @Test
    @DisplayName("obtenerOCrearPorNombreGiro crea uno nuevo cuando no existe")
    void obtenerOCrear_nuevo() {
        GiroJpaEntity guardado = entity(1L, null, "Comercio", null);
        Giro esperado = giro(1L, null, "Comercio", null, null);

        when(giroRepository.findByNombreGiroIgnoreCase("Comercio")).thenReturn(Optional.empty());
        when(giroRepository.save(any(GiroJpaEntity.class))).thenReturn(guardado);
        when(giroMapper.toDomain(guardado)).thenReturn(esperado);

        Optional<Giro> resultado = giroServiceImpl.obtenerOCrearPorNombreGiro("Comercio");

        assertThat(resultado).contains(esperado);
        verify(giroRepository).save(argThat(e -> e.getNombreGiro().equals("Comercio")));
    }

    @Test
    @DisplayName("obtenerOCrearPorNombreGiro recupera el existente si ocurre una violación de integridad al guardar")
    void obtenerOCrear_violacionIntegridad() {
        GiroJpaEntity existente = entity(1L, null, "Comercio", null);
        Giro esperado = giro(1L, null, "Comercio", null, null);

        when(giroRepository.findByNombreGiroIgnoreCase("Comercio"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(existente));
        when(giroRepository.save(any(GiroJpaEntity.class)))
                .thenThrow(new DataIntegrityViolationException("duplicado"));
        when(giroMapper.toDomain(existente)).thenReturn(esperado);

        Optional<Giro> resultado = giroServiceImpl.obtenerOCrearPorNombreGiro("Comercio");

        assertThat(resultado).contains(esperado);
        verify(giroRepository, times(2)).findByNombreGiroIgnoreCase("Comercio");
    }
}
