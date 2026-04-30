package backend.com.shared.domain.model;

import backend.com.gestionUsuarios.role.domain.model.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permisos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true, nullable = false)
    private String nombre; // e.g. "CLIENTES_READ"

    @Column(name = "descripcion")
    private String descripcion; // e.g. "Permite ver el listado de clientes"

    @Column(name = "modulo", nullable = false)
    private String modulo; // e.g. "Clientes", "Proveedores", "Administración"

    @Builder.Default
    @ManyToMany(mappedBy = "permisos")
    private Set<Role> roles = new HashSet<>();
}
