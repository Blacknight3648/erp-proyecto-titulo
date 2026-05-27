package backend.com.gestionUsuarios.usuario.infrastructure.mapper;

import backend.com.gestionUsuarios.area.application.dto.AreaDTO;
import backend.com.gestionUsuarios.area.domain.model.Area;
import backend.com.gestionUsuarios.role.application.dto.RoleDTO;
import backend.com.gestionUsuarios.role.domain.model.Role;
import backend.com.gestionUsuarios.usuario.domain.model.User;
import backend.com.gestionUsuarios.usuario.application.dto.CreateUserDTO;
import backend.com.gestionUsuarios.usuario.application.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

        public UserDTO toUserDTO(User user) {
                if (user == null)
                        return null;

                Set<RoleDTO> roles = null;
                if (user.getRoles() != null) {
                        roles = user.getRoles()
                                        .stream()
                                        .map(role -> RoleDTO.builder()
                                                        .id(role.getId())
                                                        .nombre(role.getNombre())
                                                        .descripcion(role.getDescripcion())
                                                        .build())
                                        .collect(Collectors.toSet());
                }

                Set<AreaDTO> areas = null;
                if (user.getAreas() != null) {
                        areas = user.getAreas()
                                        .stream()
                                        .map(area -> AreaDTO.builder()
                                                        .areaId(area.getAreaId())
                                                        .nombre(area.getNombre())
                                                        .descripcion(area.getDescripcion())
                                                        .build())
                                        .collect(Collectors.toSet());
                }

                return UserDTO.builder()
                                .usuarioId(user.getUsuarioId())
                                .usuarioRun(user.getUsuarioRun())
                                .usuarioNombre(user.getUsuarioNombre())
                                .usuarioApellidos(user.getUsuarioApellidos())
                                .usuarioEmail(user.getUsuarioEmail())
                                .telefono(user.getTelefono())
                                .enabled(user.isEnabled())
                                .roles(roles)
                                .areas(areas)
                                .build();
        }

        public User toUser(UserDTO dto) {
                if (dto == null)
                        return null;

                return User.builder()
                                .usuarioId(dto.getUsuarioId())
                                .usuarioRun(dto.getUsuarioRun())
                                .usuarioNombre(dto.getUsuarioNombre())
                                .usuarioApellidos(dto.getUsuarioApellidos())
                                .usuarioEmail(dto.getUsuarioEmail())
                                .usuarioPassword(dto.getUsuarioPassword())
                                .telefono(dto.getTelefono())
                                .enabled(dto.isEnabled())
                                .build();
        }

        public User toUser(CreateUserDTO dto) {
                if (dto == null)
                        return null;

                return User.builder()
<<<<<<< HEAD
                                .usuarioRun(dto.getUsuarioRun())
                                .usuarioNombre(dto.getUsuarioNombre())
                                .usuarioApellidos(dto.getUsuarioApellidos())
                                .usuarioEmail(dto.getUsuarioEmail())
                                .usuarioPassword(dto.getUsuarioPassword())
                                .telefono(dto.getTelefono())
                                .enabled(true)
                                .build();
=======
                        .usuarioRun(dto.getUsuarioRun())
                        .usuarioNombre(dto.getUsuarioNombre())
                        .usuarioApellidos(dto.getUsuarioApellidos())
                        .usuarioEmail(dto.getUsuarioEmail())
                        .usuarioPassword(dto.getUsuarioPassword())
                        .telefono(dto.getTelefono())
                        .fechaNacimiento(dto.getFechaNacimiento())
                        .direccion(dto.getDireccion())
                        .region(dto.getRegion())
                        .comuna(dto.getComuna())
                        .enabled(dto.getEnabled() != null ? dto.getEnabled() : true)
                        .build();
>>>>>>> origin/feature-docker
        }

        public Set<Role> toRoleEntities(Set<String> roles) {
                if (roles == null)
                        return null;

                return roles.stream()
                                .map(nombre -> Role.builder().nombre(nombre).build())
                                .collect(Collectors.toSet());
        }

        public Set<Area> toAreaEntities(Set<String> areas) {
                if (areas == null)
                        return null;

                return areas.stream()
                                .map(nombre -> Area.builder().nombre(nombre).build())
                                .collect(Collectors.toSet());
        }
}