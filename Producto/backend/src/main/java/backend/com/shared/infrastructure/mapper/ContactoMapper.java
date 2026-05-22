package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ContactoDTO;
import backend.com.shared.domain.model.Contacto;
import backend.com.shared.infrastructure.persistence.entity.ContactoJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ContactoMapper {

    private final TipoContactoMapper tipoContactoMapper;

    public ContactoMapper(TipoContactoMapper tipoContactoMapper) {
        this.tipoContactoMapper = tipoContactoMapper;
    }

    public Contacto toDomain(ContactoJpaEntity entity) {
        if (entity == null)
            return null;
        return Contacto.builder()
                .contactoId(entity.getContactoId())
                .nombreContacto(entity.getNombreContacto())
                .telefonoContacto(entity.getTelefonoContacto())
                .emailContacto(entity.getEmailContacto())
                .tipoContacto(tipoContactoMapper.toDomain(entity.getTipoContacto()))
                .build();
    }

    public ContactoJpaEntity toEntity(Contacto domain) {
        if (domain == null)
            return null;
        ContactoJpaEntity entity = new ContactoJpaEntity();
        entity.setContactoId(domain.getContactoId());
        entity.setNombreContacto(domain.getNombreContacto());
        entity.setTelefonoContacto(domain.getTelefonoContacto());
        entity.setEmailContacto(domain.getEmailContacto());
        entity.setTipoContacto(tipoContactoMapper.toEntity(domain.getTipoContacto()));
        return entity;
    }

    public ContactoDTO toDTO(Contacto domain) {
        if (domain == null)
            return null;
        return ContactoDTO.builder()
                .idContacto(domain.getContactoId())
                .nombreContacto(domain.getNombreContacto())
                .telefonoContacto(domain.getTelefonoContacto())
                .emailContacto(domain.getEmailContacto())
                .tipoContacto(tipoContactoMapper.toDTO(domain.getTipoContacto()))
                .build();
    }

    public Contacto toDomain(ContactoDTO dto) {
        if (dto == null)
            return null;
        return Contacto.builder()
                .contactoId(dto.getIdContacto())
                .nombreContacto(dto.getNombreContacto())
                .telefonoContacto(dto.getTelefonoContacto())
                .emailContacto(dto.getEmailContacto())
                .tipoContacto(tipoContactoMapper.toDomain(dto.getTipoContacto()))
                .build();
    }
}
