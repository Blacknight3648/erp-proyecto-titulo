package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contador atómico de números de documento por tipo (NV, EVN, OP, SCOS, SCOT, ...).
 *
 * El acceso a esta tabla se hace siempre con bloqueo pesimista (SELECT ... FOR UPDATE),
 * garantizando que dos transacciones concurrentes que pidan el siguiente número del
 * mismo tipo se serializan: una espera a que la otra termine antes de leer.
 */
@Entity
@Table(name = "document_counter")
@Getter
@Setter
@NoArgsConstructor
public class DocumentCounterJpaEntity {

    @Id
    @Column(length = 20)
    private String tipo;

    @Column(name = "ultimo_numero", nullable = false)
    private Long ultimoNumero;

    public DocumentCounterJpaEntity(String tipo, Long ultimoNumero) {
        this.tipo = tipo;
        this.ultimoNumero = ultimoNumero;
    }
}
