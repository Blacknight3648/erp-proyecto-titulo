package backend.com.shared.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

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
}
