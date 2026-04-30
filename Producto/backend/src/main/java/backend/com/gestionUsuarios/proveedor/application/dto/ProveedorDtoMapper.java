package backend.com.gestionUsuarios.proveedor.application.dto;

import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProveedorDtoMapper {

    public ProveedorDTO toDto(Proveedor domain) {

        if (domain == null) {
            return null;
        }

        return ProveedorDTO.builder()
                .proveedorId(domain.getProveedorId())
                .nombreProveedor(domain.getNombreProveedor())
                .rutProveedor(domain.getRutProveedor())
                .direccionProveedor(domain.getDireccionProveedor())
                .telefonoProveedor(domain.getTelefonoProveedor())
                .emailProveedor(domain.getEmailProveedor())
                .contactoProveedor(domain.getContactoProveedor())
                .categoria(domain.getCategoria())
                .activo(domain.isActivo())
                .build();
    }

    public Proveedor toDomain(ProveedorDTO dto) {

        if (dto == null) {
            return null;
        }

        return Proveedor.builder()
                .proveedorId(dto.getProveedorId())
                .nombreProveedor(dto.getNombreProveedor())
                .rutProveedor(dto.getRutProveedor())
                .direccionProveedor(dto.getDireccionProveedor())
                .telefonoProveedor(dto.getTelefonoProveedor())
                .emailProveedor(dto.getEmailProveedor())
                .contactoProveedor(dto.getContactoProveedor())
                .categoria(dto.getCategoria())
                .activo(dto.isActivo())
                .build();
    }

    public List<ProveedorDTO> toDtoList(List<Proveedor> domains) {
        return domains.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}