package backend.com.shared.application.service;

import backend.com.shared.application.dto.ContactoDTO;
import backend.com.shared.application.dto.TipoContactoDTO;

import java.util.List;
import java.util.Optional;

public interface ContactoService {

    List<ContactoDTO> getAllContactos();

    Optional<ContactoDTO> getContactoById(Long id);

    ContactoDTO createContacto(ContactoDTO contactoDTO);

    Optional<ContactoDTO> updateContacto(Long id, ContactoDTO contactoDTO);

    void deleteContacto(Long id);

    List<TipoContactoDTO> getAllTiposContacto();

}
