package backend.com.shared.infrastructure.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import backend.com.shared.application.dto.ContactoDTO;
import backend.com.shared.application.service.ContactoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/contacto")
@RequiredArgsConstructor
public class ContactoController {

    private final ContactoService contactoService;

    @GetMapping
    public List<ContactoDTO> getAllContactos() {
        return contactoService.getAllContactos();
    }

    @GetMapping("/{id}")
    public ContactoDTO getContactoById(@PathVariable Long id) {
        return contactoService.getContactoById(id).orElse(null);
    }

    @PostMapping
    public ContactoDTO createContacto(@RequestBody ContactoDTO contactoDTO) {
        return contactoService.createContacto(contactoDTO);
    }

    @PutMapping("/{id}")
    public ContactoDTO updateContacto(@PathVariable Long id, @RequestBody ContactoDTO contactoDTO) {
        return contactoService.updateContacto(id, contactoDTO).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteContacto(@PathVariable Long id) {
        contactoService.deleteContacto(id);
    }

}
