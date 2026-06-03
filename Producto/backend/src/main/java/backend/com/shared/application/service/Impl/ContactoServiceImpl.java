package backend.com.shared.application.service.impl;

import backend.com.shared.application.service.ContactoService;
import backend.com.shared.infrastructure.persistence.repository.ContactoRepository;
import backend.com.shared.infrastructure.persistence.repository.TipoContactoRepository;
import lombok.RequiredArgsConstructor;
import backend.com.shared.application.dto.ContactoDTO;
import backend.com.shared.application.dto.TipoContactoDTO;
import backend.com.shared.infrastructure.mapper.ContactoMapper;
import backend.com.shared.infrastructure.mapper.TipoContactoMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactoServiceImpl implements ContactoService {

    private final ContactoRepository contactoRepository;
    private final TipoContactoRepository tipoContactoRepository;
    private final ContactoMapper contactoMapper;
    private final TipoContactoMapper tipoContactoMapper;

    @Override
    public List<ContactoDTO> getAllContactos() {
        return contactoRepository.findAll().stream()
                .map(entity -> contactoMapper.toDTO(contactoMapper.toDomain(entity)))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ContactoDTO> getContactoById(Long id) {
        return contactoRepository.findById(id)
                .map(entity -> contactoMapper.toDTO(contactoMapper.toDomain(entity)));
    }

    @Override
    public ContactoDTO createContacto(ContactoDTO contactoDTO) {
        var entity = contactoMapper.toEntity(contactoMapper.toDomain(contactoDTO));
        var saved = contactoRepository.save(entity);
        return contactoMapper.toDTO(contactoMapper.toDomain(saved));
    }

    @Override
    public Optional<ContactoDTO> updateContacto(Long id, ContactoDTO contactoDTO) {
        contactoDTO.setIdContacto(id);
        return contactoRepository.findById(id).map(contacto -> {
            var domainToUpdate = contactoMapper.toDomain(contactoDTO);
            contacto.setNombreContacto(domainToUpdate.getNombreContacto());
            contacto.setTelefonoContacto(domainToUpdate.getTelefonoContacto());
            contacto.setEmailContacto(domainToUpdate.getEmailContacto());
            
            if (domainToUpdate.getTipoContacto() != null && domainToUpdate.getTipoContacto().getTipoContactoId() != null) {
                var tipoContacto = tipoContactoRepository.findById(domainToUpdate.getTipoContacto().getTipoContactoId())
                    .orElseThrow(() -> new RuntimeException("Tipo de contacto no encontrado"));
                contacto.setTipoContacto(tipoContacto);
            }
            
            var saved = contactoRepository.save(contacto);
            return contactoMapper.toDTO(contactoMapper.toDomain(saved));
        });
    }

    @Override
    public void deleteContacto(Long id) {
        contactoRepository.deleteById(id);
    }

    @Override
    public List<TipoContactoDTO> getAllTiposContacto() {
        return tipoContactoRepository.findAll().stream()
                .map(entity -> tipoContactoMapper.toDTO(tipoContactoMapper.toDomain(entity)))
                .collect(Collectors.toList());
    }

}
