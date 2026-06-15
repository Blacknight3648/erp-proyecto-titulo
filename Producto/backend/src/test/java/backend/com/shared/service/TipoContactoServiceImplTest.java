package backend.com.shared.service;

import backend.com.shared.application.dto.TipoContactoDTO;
import backend.com.shared.application.service.impl.TipoContactoServiceImpl;
import backend.com.shared.domain.model.TipoContacto;
import backend.com.shared.infrastructure.mapper.TipoContactoMapper;
import backend.com.shared.infrastructure.persistence.entity.TipoContactoJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.TipoContactoRepository;
import jakarta.persistence.EntityNotFoundException;
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
@DisplayName("TipoContactoServiceImpl")
class TipoContactoServiceImplTest {

    @Mock
    private TipoContactoRepository tipoContactoRepository;

    @Mock
    private TipoContactoMapper tipoContactoMapper;

    @InjectMocks
    private TipoContactoServiceImpl tipoContactoServiceImpl;

    // ---------------- HELPERS ----------------

    private TipoContactoJpaEntity entity(Long id, String descripcion) {
        return TipoContactoJpaEntity.builder()
                .tipoContactoId(id)
                .descripcionTipoContacto(descripcion)
                .build();
    }

    private TipoContacto domain(Long id, String descripcion) {
        return TipoContacto.builder()
                .tipoContactoId(id)
                .descripcionTipoContacto(descripcion)
                .build();
    }

    private TipoContactoDTO dto(Long id, String descripcion) {
        TipoContactoDTO dto = new TipoContactoDTO();
        dto.setTipoContactoId(id);
        dto.setDescripcionTipoContacto(descripcion);
        return dto;
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        TipoContactoJpaEntity e1 = entity(1L, "Cliente");
        TipoContactoJpaEntity e2 = entity(2L, "Proveedor");
        TipoContacto d1 = domain(1L, "Cliente");
        TipoContacto d2 = domain(2L, "Proveedor");
        TipoContactoDTO dto1 = dto(1L, "Cliente");
        TipoContactoDTO dto2 = dto(2L, "Proveedor");

        when(tipoContactoRepository.findAll()).thenReturn(List.of(e1, e2));
        when(tipoContactoMapper.toDomain(e1)).thenReturn(d1);
        when(tipoContactoMapper.toDomain(e2)).thenReturn(d2);
        when(tipoContactoMapper.toDTO(d1)).thenReturn(dto1);
        when(tipoContactoMapper.toDTO(d2)).thenReturn(dto2);

        List<TipoContactoDTO> resultado = tipoContactoServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el tipo de contacto existe")
    void obtenerPorId_ok() {
        TipoContactoJpaEntity e1 = entity(1L, "Cliente");
        TipoContacto d1 = domain(1L, "Cliente");
        TipoContactoDTO dto1 = dto(1L, "Cliente");

        when(tipoContactoRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(tipoContactoMapper.toDomain(e1)).thenReturn(d1);
        when(tipoContactoMapper.toDTO(d1)).thenReturn(dto1);

        Optional<TipoContactoDTO> resultado = tipoContactoServiceImpl.obtenerPorId(1L);

        assertThat(resultado).contains(dto1);
    }

    @Test
    @DisplayName("obtenerPorId retorna vacío si el tipo de contacto no existe")
    void obtenerPorId_noExiste() {
        when(tipoContactoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<TipoContactoDTO> resultado = tipoContactoServiceImpl.obtenerPorId(1L);

        assertThat(resultado).isEmpty();
    }

    // ---------------- obtenerPorDescripcionTipoContacto ----------------

    @Test
    @DisplayName("obtenerPorDescripcionTipoContacto retorna el DTO cuando existe")
    void obtenerPorDescripcionTipoContacto_ok() {
        TipoContactoJpaEntity e1 = entity(1L, "Cliente");
        TipoContacto d1 = domain(1L, "Cliente");
        TipoContactoDTO dto1 = dto(1L, "Cliente");

        when(tipoContactoRepository.findByDescripcionTipoContactoIgnoreCase("Cliente")).thenReturn(Optional.of(e1));
        when(tipoContactoMapper.toDomain(e1)).thenReturn(d1);
        when(tipoContactoMapper.toDTO(d1)).thenReturn(dto1);

        Optional<TipoContactoDTO> resultado = tipoContactoServiceImpl.obtenerPorDescripcionTipoContacto("Cliente");

        assertThat(resultado).contains(dto1);
    }

    @Test
    @DisplayName("obtenerPorDescripcionTipoContacto retorna vacío si no existe")
    void obtenerPorDescripcionTipoContacto_noExiste() {
        when(tipoContactoRepository.findByDescripcionTipoContactoIgnoreCase("Inexistente")).thenReturn(Optional.empty());

        Optional<TipoContactoDTO> resultado = tipoContactoServiceImpl.obtenerPorDescripcionTipoContacto("Inexistente");

        assertThat(resultado).isEmpty();
    }

    // ---------------- crearTipoContacto ----------------

    @Test
    @DisplayName("crearTipoContacto mapea, guarda y retorna el DTO resultante")
    void crearTipoContacto_ok() {
        TipoContactoDTO dtoEntrada = dto(null, "Cliente");
        TipoContacto domainEntrada = domain(null, "Cliente");
        TipoContactoJpaEntity entidadEntrada = entity(null, "Cliente");
        TipoContactoJpaEntity entidadGuardada = entity(1L, "Cliente");
        TipoContacto domainGuardado = domain(1L, "Cliente");
        TipoContactoDTO esperado = dto(1L, "Cliente");

        when(tipoContactoMapper.toDomain(dtoEntrada)).thenReturn(domainEntrada);
        when(tipoContactoMapper.toEntity(domainEntrada)).thenReturn(entidadEntrada);
        when(tipoContactoRepository.save(entidadEntrada)).thenReturn(entidadGuardada);
        when(tipoContactoMapper.toDomain(entidadGuardada)).thenReturn(domainGuardado);
        when(tipoContactoMapper.toDTO(domainGuardado)).thenReturn(esperado);

        TipoContactoDTO resultado = tipoContactoServiceImpl.crearTipoContacto(dtoEntrada);

        assertThat(resultado).isEqualTo(esperado);
    }

    // ---------------- actualizarTipoContacto ----------------

    @Test
    @DisplayName("actualizarTipoContacto modifica la descripción cuando el tipo de contacto existe")
    void actualizarTipoContacto_ok() {
        TipoContactoJpaEntity existente = entity(1L, "Cliente");
        TipoContactoDTO dtoEntrada = dto(null, "Proveedor");
        TipoContacto domainActualizado = domain(null, "Proveedor");
        TipoContacto domainGuardado = domain(1L, "Proveedor");
        TipoContactoDTO esperado = dto(1L, "Proveedor");

        when(tipoContactoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoContactoMapper.toDomain(dtoEntrada)).thenReturn(domainActualizado);
        when(tipoContactoRepository.save(existente)).thenReturn(existente);
        when(tipoContactoMapper.toDomain(existente)).thenReturn(domainGuardado);
        when(tipoContactoMapper.toDTO(domainGuardado)).thenReturn(esperado);

        TipoContactoDTO resultado = tipoContactoServiceImpl.actualizarTipoContacto(1L, dtoEntrada);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getDescripcionTipoContacto()).isEqualTo("Proveedor");
    }

    @Test
    @DisplayName("actualizarTipoContacto lanza excepción si el tipo de contacto no existe")
    void actualizarTipoContacto_noExiste() {
        TipoContactoDTO dtoEntrada = dto(null, "Proveedor");

        when(tipoContactoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tipoContactoServiceImpl.actualizarTipoContacto(1L, dtoEntrada))
                .isInstanceOf(EntityNotFoundException.class);

        verify(tipoContactoRepository, never()).save(any());
    }

    // ---------------- eliminarTipoContacto ----------------

    @Test
    @DisplayName("eliminarTipoContacto elimina correctamente cuando el tipo de contacto existe")
    void eliminarTipoContacto_ok() {
        when(tipoContactoRepository.existsById(1L)).thenReturn(true);

        tipoContactoServiceImpl.eliminarTipoContacto(1L);

        verify(tipoContactoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarTipoContacto lanza excepción si el tipo de contacto no existe")
    void eliminarTipoContacto_noExiste() {
        when(tipoContactoRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> tipoContactoServiceImpl.eliminarTipoContacto(1L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(tipoContactoRepository, never()).deleteById(any());
    }

    // ---------------- obtenerOCrearPorDescripcionTipoContacto ----------------

    @Test
    @DisplayName("obtenerOCrearPorDescripcionTipoContacto retorna vacío si la descripción está en blanco")
    void obtenerOCrear_blanco() {
        Optional<TipoContactoDTO> resultado = tipoContactoServiceImpl.obtenerOCrearPorDescripcionTipoContacto("   ");

        assertThat(resultado).isEmpty();
        verifyNoInteractions(tipoContactoRepository);
    }

    @Test
    @DisplayName("obtenerOCrearPorDescripcionTipoContacto retorna el existente cuando ya está registrado")
    void obtenerOCrear_existente() {
        TipoContactoJpaEntity existente = entity(1L, "Cliente");
        TipoContacto domainExistente = domain(1L, "Cliente");
        TipoContactoDTO esperado = dto(1L, "Cliente");

        when(tipoContactoRepository.findByDescripcionTipoContactoIgnoreCase("Cliente")).thenReturn(Optional.of(existente));
        when(tipoContactoMapper.toDomain(existente)).thenReturn(domainExistente);
        when(tipoContactoMapper.toDTO(domainExistente)).thenReturn(esperado);

        Optional<TipoContactoDTO> resultado = tipoContactoServiceImpl.obtenerOCrearPorDescripcionTipoContacto(" Cliente ");

        assertThat(resultado).contains(esperado);
        verify(tipoContactoRepository, never()).save(any());
    }

    @Test
    @DisplayName("obtenerOCrearPorDescripcionTipoContacto crea uno nuevo cuando no existe")
    void obtenerOCrear_nuevo() {
        TipoContactoJpaEntity guardado = entity(1L, "Cliente");
        TipoContacto domainGuardado = domain(1L, "Cliente");
        TipoContactoDTO esperado = dto(1L, "Cliente");

        when(tipoContactoRepository.findByDescripcionTipoContactoIgnoreCase("Cliente")).thenReturn(Optional.empty());
        when(tipoContactoRepository.save(any(TipoContactoJpaEntity.class))).thenReturn(guardado);
        when(tipoContactoMapper.toDomain(guardado)).thenReturn(domainGuardado);
        when(tipoContactoMapper.toDTO(domainGuardado)).thenReturn(esperado);

        Optional<TipoContactoDTO> resultado = tipoContactoServiceImpl.obtenerOCrearPorDescripcionTipoContacto("Cliente");

        assertThat(resultado).contains(esperado);
        verify(tipoContactoRepository).save(argThat(e -> e.getDescripcionTipoContacto().equals("Cliente")));
    }

    @Test
    @DisplayName("obtenerOCrearPorDescripcionTipoContacto recupera el existente si ocurre una violación de integridad al guardar")
    void obtenerOCrear_violacionIntegridad() {
        TipoContactoJpaEntity existente = entity(1L, "Cliente");
        TipoContacto domainExistente = domain(1L, "Cliente");
        TipoContactoDTO esperado = dto(1L, "Cliente");

        when(tipoContactoRepository.findByDescripcionTipoContactoIgnoreCase("Cliente"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(existente));
        when(tipoContactoRepository.save(any(TipoContactoJpaEntity.class)))
                .thenThrow(new DataIntegrityViolationException("duplicado"));
        when(tipoContactoMapper.toDomain(existente)).thenReturn(domainExistente);
        when(tipoContactoMapper.toDTO(domainExistente)).thenReturn(esperado);

        Optional<TipoContactoDTO> resultado = tipoContactoServiceImpl.obtenerOCrearPorDescripcionTipoContacto("Cliente");

        assertThat(resultado).contains(esperado);
        verify(tipoContactoRepository, times(2)).findByDescripcionTipoContactoIgnoreCase("Cliente");
    }
}
