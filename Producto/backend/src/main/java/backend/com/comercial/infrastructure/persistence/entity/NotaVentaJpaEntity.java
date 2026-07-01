package backend.com.comercial.infrastructure.persistence.entity;

import backend.com.gestionUsuarios.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.AuditableJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notas_venta")
@Getter
@Setter
public class NotaVentaJpaEntity extends AuditableJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nv")
    private Long idNV;

    @Column(unique = true, length = 20, nullable = false)
    private String numeroNV;

    @Column(name = "evaluacion_negocio_id", nullable = false)
    private Long evaluacionNegocioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteJpaEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    private VendedorJpaEntity vendedor;

    @Column(length = 20, nullable = false)
    private String estado;

    @Column(name = "es_kit")
    private Boolean esKit;

    @Column(name = "detalle_kit", length = 500)
    private String detalleKit;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_entrega_estimada")
    private LocalDate fechaEntregaEstimada;

    @Column(name = "monto_subtotal", precision = 12, scale = 2)
    private BigDecimal montoSubtotal;

    @Column(length = 3)
    private String monedaSubtotal;

    @Column(name = "monto_iva", precision = 12, scale = 2)
    private BigDecimal montoIva;

    @Column(length = 3)
    private String monedaIva;

    @Column(name = "monto_total", precision = 12, scale = 2)
    private BigDecimal montoTotal;

    @Column(length = 3)
    private String monedaTotal;

    // Se declara la lista 'items' con la relación correcta ---
    @OneToMany(mappedBy = "notaVenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaVentaItemJpaEntity> items = new ArrayList<>();

    // --- Métodos Helper para mantener la consistencia de la relación ---
    public void addItem(NotaVentaItemJpaEntity item) {
        items.add(item);
        item.setNotaVenta(this);
    }

    public void removeItem(NotaVentaItemJpaEntity item) {
        items.remove(item);
        item.setNotaVenta(null);
    }
}