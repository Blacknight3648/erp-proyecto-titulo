package backend.com.shared.service;

import backend.com.shared.application.dto.ContactoDTO;
import backend.com.shared.application.dto.TipoContactoDTO;
import backend.com.shared.application.service.impl.ContactoServiceImpl;
import backend.com.shared.domain.model.Contacto;
import backend.com.shared.domain.model.TipoContacto;
import backend.com.shared.infrastructure.mapper.ContactoMapper;
import backend.com.shared.infrastructure.mapper.TipoContactoMapper;
import backend.com.shared.infrastructure.persistence.entity.ContactoJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.TipoContactoJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.ContactoRepository;
import backend.com.shared.infrastructure.persistence.repository.TipoContactoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContactoServiceImpl")
class ContactoServiceImplTest {

    @Mock
    private ContactoRepository contactoRepository;

    @Mock
    private TipoContactoRepository tipoContactoRepository;

    @Mock
    private ContactoMapper contactoMapper;

    @Mock
    private TipoContactoMapper tipoContactoMapper;

    @InjectMocks
    private ContactoServiceImpl contactoServiceImpl;

    // ---------------- HELPERS ----------------

    private ContactoJpaEntity entity(Long id, String nombre, String telefono, String email, TipoContactoJpaEntity tipoContacto) {
        return ContactoJpaEntity.builder()
                .contactoId(id)
                .nombreContacto(nombre)
                .telefonoContacto(telefono)
                .emailContacto(email)
                .tipoContacto(tipoContacto)
                .build();
    }

    private Contacto domain(Long id, String nombre, String telefono, String email, TipoContacto tipoContacto) {
        return Contacto.builder()
                .contactoId(id)
                .nombreContacto(nombre)
                .telefonoContacto(telefono)
                .emailContacto(email)
                .tipoContacto(tipoContacto)
                .build();
    }

    private ContactoDTO dto(Long id, String nombre, String telefono, String email, TipoContactoDTO tipoContacto) {
        return ContactoDTO.builder()
                .idContacto(id)
                .nombreContacto(nombre)
                .telefonoContacto(telefono)
                .emailContacto(email)
                .tipoContacto(tipoContacto)
                .build();
    }

    private TipoContactoJpaEntity tipoContactoEntity(Long id, String descripcion) {
        return TipoContactoJpaEntity.builder()
                .tipoContactoId(id)
                .descripcionTipoContacto(descripcion)
                .build();
    }

    // ---------------- getAllContactos ----------------

    @Test
    @DisplayName("getAllContactos mapea correctamente la lista completa")
    void getAllContactos_ok() {
        ContactoJpaEntity e1 = entity(1L, "Juan", "123", "juan@mail.com", null);
        Contacto d1 = domain(1L, "Juan", "123", "juan@mail.com", null);
        ContactoDTO dto1 = dto(1L, "Juan", "123", "juan@mail.com", null);

        when(contactoRepository.findAll()).thenReturn(List.of(e1));
        when(contactoMapper.toDomain(e1)).thenReturn(d1);
        when(contactoMapper.toDTO(d1)).thenReturn(dto1);

        List<ContactoDTO> resultado = contactoServiceImpl.getAllContactos();

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- getContactoById ----------------

    @Test
    @DisplayName("getContactoById retorna el DTO cuando el contacto existe")
    void getContactoById_ok() {
        ContactoJpaEntity e1 = entity(1L, "Juan", "123", "juan@mail.com", null);
        Contacto d1 = domain(1L, "Juan", "123", "juan@mail.com", null);
        ContactoDTO dto1 = dto(1L, "Juan", "123", "juan@mail.com", null);

        when(contactoRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(contactoMapper.toDomain(e1)).thenReturn(d1);
        when(contactoMapper.toDTO(d1)).thenReturn(dto1);

        Optional<ContactoDTO> resultado = contactoServiceImpl.getContactoById(1L);

        assertThat(resultado).contains(dto1);
    }

    @Test
    @DisplayName("getContactoById retorna vacío si el contacto no existe")
    void getContactoById_noExiste() {
        when(contactoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ContactoDTO> resultado = contactoServiceImpl.getContactoById(1L);

        assertThat(resultado).isEmpty();
    }

    // ---------------- createContacto ----------------

    @Test
    @DisplayName("createContacto mapea, guarda y retorna el DTO resultante")
    void createContacto_ok() {
        ContactoDTO dtoEntrada = dto(null, "Juan", "123", "juan@mail.com", null);
        Contacto domainEntrada = domain(null, "Juan", "123", "juan@mail.com", null);
        ContactoJpaEntity entidadEntrada = entity(null, "Juan", "123", "juan@mail.com", null);
        ContactoJpaEntity entidadGuardada = entity(1L, "Juan", "123", "juan@mail.com", null);
        Contacto domainGuardado = domain(1L, "Juan", "123", "juan@mail.com", null);
        ContactoDTO esperado = dto(1L, "Juan", "123", "juan@mail.com", null);

        when(contactoMapper.toDomain(dtoEntrada)).thenReturn(domainEntrada);
        when(contactoMapper.toEntity(domainEntrada)).thenReturn(entidadEntrada);
        when(contactoRepository.save(entidadEntrada)).thenReturn(entidadGuardada);
        when(contactoMapper.toDomain(entidadGuardada)).thenReturn(domainGuardado);
        when(contactoMapper.toDTO(domainGuardado)).thenReturn(esperado);

        ContactoDTO resultado = contactoServiceImpl.createContacto(dtoEntrada);

        assertThat(resultado).isEqualTo(esperado);
    }

    // ---------------- updateContacto ----------------

    @Test
    @DisplayName("updateContacto modifica los datos cuando el contacto existe y no cambia el tipo de contacto")
    void updateContacto_ok() {
        ContactoJpaEntity existente = entity(1L, "Juan", "123", "juan@mail.com", tipoContactoEntity(1L, "Cliente"));
        ContactoDTO dtoEntrada = dto(null, "Juan Actualizado", "456", "nuevo@mail.com", null);
        Contacto domainActualizado = domain(null, "Juan Actualizado", "456", "nuevo@mail.com", null);
        Contacto domainGuardado = domain(1L, "Juan Actualizado", "456", "nuevo@mail.com", null);
        ContactoDTO esperado = dto(1L, "Juan Actualizado", "456", "nuevo@mail.com", null);

        when(contactoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(contactoMapper.toDomain(dtoEntrada)).thenReturn(domainActualizado);
        when(contactoRepository.save(existente)).thenReturn(existente);
        when(contactoMapper.toDomain(existente)).thenReturn(domainGuardado);
        when(contactoMapper.toDTO(domainGuardado)).thenReturn(esperado);

        Optional<ContactoDTO> resultado = contactoServiceImpl.updateContacto(1L, dtoEntrada);

        assertThat(resultado).contains(esperado);
        assertThat(existente.getNombreContacto()).isEqualTo("Juan Actualizado");
        assertThat(existente.getTelefonoContacto()).isEqualTo("456");
        assertThat(existente.getEmailContacto()).isEqualTo("nuevo@mail.com");
        assertThat(dtoEntrada.getIdContacto()).isEqualTo(1L);
        verify(tipoContactoRepository, never()).findById(any());
    }

    @Test
    @DisplayName("updateContacto actualiza el tipo de contacto cuando se especifica uno nuevo")
    void updateContacto_cambiaTipoContacto() {
        ContactoJpaEntity existente = entity(1L, "Juan", "123", "juan@mail.com", tipoContactoEntity(1L, "Cliente"));
        TipoContactoJpaEntity nuevoTipoEntity = tipoContactoEntity(2L, "Proveedor");
        TipoContactoDTO tipoContactoDTO = new TipoContactoDTO(2L, "Proveedor");
        TipoContacto tipoContactoDomain = TipoContacto.builder().tipoContactoId(2L).descripcionTipoContacto("Proveedor").build();
        ContactoDTO dtoEntrada = dto(null, "Juan", "123", "juan@mail.com", tipoContactoDTO);
        Contacto domainActualizado = domain(null, "Juan", "123", "juan@mail.com", tipoContactoDomain);
        Contacto domainGuardado = domain(1L, "Juan", "123", "juan@mail.com", tipoContactoDomain);
        ContactoDTO esperado = dto(1L, "Juan", "123", "juan@mail.com", tipoContactoDTO);

        when(contactoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(contactoMapper.toDomain(dtoEntrada)).thenReturn(domainActualizado);
        when(tipoContactoRepository.findById(2L)).thenReturn(Optional.of(nuevoTipoEntity));
        when(contactoRepository.save(existente)).thenReturn(existente);
        when(contactoMapper.toDomain(existente)).thenReturn(domainGuardado);
        when(contactoMapper.toDTO(domainGuardado)).thenReturn(esperado);

        Optional<ContactoDTO> resultado = contactoServiceImpl.updateContacto(1L, dtoEntrada);

        assertThat(resultado).contains(esperado);
        assertThat(existente.getTipoContacto()).isEqualTo(nuevoTipoEntity);
    }

    @Test
    @DisplayName("updateContacto lanza excepción si el tipo de contacto especificado no existe")
    void updateContacto_tipoContactoNoExiste() {
        ContactoJpaEntity existente = entity(1L, "Juan", "123", "juan@mail.com", tipoContactoEntity(1L, "Cliente"));
        TipoContactoDTO tipoContactoDTO = new TipoContactoDTO(99L, "Inexistente");
        TipoContacto tipoContactoDomain = TipoContacto.builder().tipoContactoId(99L).descripcionTipoContacto("Inexistente").build();
        ContactoDTO dtoEntrada = dto(null, "Juan", "123", "juan@mail.com", tipoContactoDTO);
        Contacto domainActualizado = domain(null, "Juan", "123", "juan@mail.com", tipoContactoDomain);

        when(contactoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(contactoMapper.toDomain(dtoEntrada)).thenReturn(domainActualizado);
        when(tipoContactoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactoServiceImpl.updateContacto(1L, dtoEntrada))
                .isInstanceOf(RuntimeException.class);

        verify(contactoRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateContacto retorna vacío si el contacto no existe")
    void updateContacto_noExiste() {
        ContactoDTO dtoEntrada = dto(null, "Juan Actualizado", "456", "nuevo@mail.com", null);

        when(contactoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ContactoDTO> resultado = contactoServiceImpl.updateContacto(1L, dtoEntrada);

        assertThat(resultado).isEmpty();
        verify(contactoRepository, never()).save(any());
    }

    // ---------------- deleteContacto ----------------

    @Test
    @DisplayName("deleteContacto invoca la eliminación por id")
    void deleteContacto_ok() {
        contactoServiceImpl.deleteContacto(1L);

        verify(contactoRepository).deleteById(1L);
    }

    // ---------------- getAllTiposContacto ----------------

    @Test
    @DisplayName("getAllTiposContacto mapea correctamente la lista completa")
    void getAllTiposContacto_ok() {
        TipoContactoJpaEntity e1 = tipoContactoEntity(1L, "Cliente");
        TipoContacto d1 = TipoContacto.builder().tipoContactoId(1L).descripcionTipoContacto("Cliente").build();
        TipoContactoDTO dto1 = new TipoContactoDTO(1L, "Cliente");

        when(tipoContactoRepository.findAll()).thenReturn(List.of(e1));
        when(tipoContactoMapper.toDomain(e1)).thenReturn(d1);
        when(tipoContactoMapper.toDTO(d1)).thenReturn(dto1);

        List<TipoContactoDTO> resultado = contactoServiceImpl.getAllTiposContacto();

        assertThat(resultado).containsExactly(dto1);
    }
}
