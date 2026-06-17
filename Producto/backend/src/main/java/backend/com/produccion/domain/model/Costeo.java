package backend.com.produccion.domain.model;

import backend.com.produccion.domain.enums.EstadoCosteo;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Costeo {
    private Long idCosteo;
    private DocumentNumber numeroCosteo;
    /** Estado del ciclo de vida. Por defecto BORRADOR al construir un costeo nuevo. */
    private EstadoCosteo estado = EstadoCosteo.BORRADOR;
    /** Motivo del último rechazo (solo poblado cuando estado == RECHAZADO). */
    private String motivoRechazo;
    private Long solicitudCostosId;
    /** Referencia suave a la NV que originó este Costeo (solo para costeos auto-creados). */
    private Long notaVentaId;
    private Long clienteId;
    private String clienteNombre;
    private Long vendedorId;
    private String vendedorNombre;

    // Detalle de costos (Money para precios, BigDecimal para cantidades)
    private Money costoHilos;
    private Money costoManoObra;
    private Money costoEtiquetas;
    private Money costoEmbalaje;
    private Money costoFlete;

    private BigDecimal porcentajeCostoFijo;

    // Cintas y Vivos
    private Money precioCinta1;
    private BigDecimal cantidadCinta1;
    private Money precioCinta2;
    private BigDecimal cantidadCinta2;
    private Money vivoReflectivo;
    private BigDecimal cantidadVivo;

    private Money costoTotalMateriaPrima;
    private BigDecimal margenBrutoSugerido;
    private Money precioVentaSugerido;
    private java.util.List<CosteoItem> items = new java.util.ArrayList<>();

    /**
     * Crea un Costeo con todos los valores en cero, listo para ser completado por el equipo
     * de producción. Se usa cuando una NV contiene ítems OP sin Costeo pre-vinculado en la EVN.
     */
    public static Costeo crearVacio(DocumentNumber numero, Long notaVentaId) {
        Costeo c = new Costeo();
        c.numeroCosteo = numero;
        c.notaVentaId = notaVentaId;
        c.solicitudCostosId = null;
        c.costoHilos = new Money(java.math.BigDecimal.ZERO, "CLP");
        c.costoManoObra = new Money(java.math.BigDecimal.ZERO, "CLP");
        c.costoEtiquetas = new Money(java.math.BigDecimal.ZERO, "CLP");
        c.costoEmbalaje = new Money(java.math.BigDecimal.ZERO, "CLP");
        c.costoFlete = new Money(java.math.BigDecimal.ZERO, "CLP");
        c.porcentajeCostoFijo = java.math.BigDecimal.ZERO;
        c.precioCinta1 = new Money(java.math.BigDecimal.ZERO, "CLP");
        c.cantidadCinta1 = java.math.BigDecimal.ZERO;
        c.precioCinta2 = new Money(java.math.BigDecimal.ZERO, "CLP");
        c.cantidadCinta2 = java.math.BigDecimal.ZERO;
        c.vivoReflectivo = new Money(java.math.BigDecimal.ZERO, "CLP");
        c.cantidadVivo = java.math.BigDecimal.ZERO;
        c.costoTotalMateriaPrima = new Money(java.math.BigDecimal.ZERO, "CLP");
        c.margenBrutoSugerido = java.math.BigDecimal.ZERO;
        c.precioVentaSugerido = new Money(java.math.BigDecimal.ZERO, "CLP");
        c.items = new java.util.ArrayList<>();
        return c;
    }

    public Costeo(Long id, DocumentNumber numero, Long solicitudCostosId, 
            Long clienteId, String clienteNombre, Long vendedorId, String vendedorNombre,
            Money costoHilos, Money costoManoObra,
            Money costoEtiquetas, Money costoEmbalaje, Money costoFlete,
            BigDecimal porcentajeCostoFijo, Money precioCinta1, BigDecimal cantidadCinta1,
            Money precioCinta2, BigDecimal cantidadCinta2, Money vivoReflectivo,
            BigDecimal cantidadVivo, Money costoTotalMateriaPrima,
            BigDecimal margenBrutoSugerido, Money precioVentaSugerido,
            java.util.List<CosteoItem> items) {
        this.idCosteo = id;
        this.numeroCosteo = numero;
        this.solicitudCostosId = solicitudCostosId;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.vendedorId = vendedorId;
        this.vendedorNombre = vendedorNombre;
        this.costoHilos = costoHilos;
        this.costoManoObra = costoManoObra;
        this.costoEtiquetas = costoEtiquetas;
        this.costoEmbalaje = costoEmbalaje;
        this.costoFlete = costoFlete;
        this.porcentajeCostoFijo = porcentajeCostoFijo;
        this.precioCinta1 = precioCinta1;
        this.cantidadCinta1 = cantidadCinta1;
        this.precioCinta2 = precioCinta2;
        this.cantidadCinta2 = cantidadCinta2;
        this.vivoReflectivo = vivoReflectivo;
        this.cantidadVivo = cantidadVivo;
        this.costoTotalMateriaPrima = costoTotalMateriaPrima;
        this.margenBrutoSugerido = margenBrutoSugerido;
        this.precioVentaSugerido = precioVentaSugerido;
        this.items = items != null ? items : new java.util.ArrayList<>();
    }

    // ----------------------------------------------------------------------
    // Transiciones de estado (ciclo de vida del Costeo)
    // ----------------------------------------------------------------------

    private void transicionarA(EstadoCosteo destino) {
        EstadoCosteo actual = this.estado != null ? this.estado : EstadoCosteo.BORRADOR;
        if (!actual.puedeTransicionarA(destino)) {
            throw new BusinessRuleException(
                    "Transición de estado inválida para el Costeo: " + actual + " → " + destino);
        }
        this.estado = destino;
    }

    /** Producción confirma los costos: BORRADOR → COSTEADO. */
    public void marcarCosteado() {
        transicionarA(EstadoCosteo.COSTEADO);
    }

    /** Aprobación que habilita el vínculo con EVN/NV: COSTEADO → APROBADO. */
    public void aprobar() {
        transicionarA(EstadoCosteo.APROBADO);
    }

    /**
     * Rechaza el costeo por diferencias. Exige un motivo y lo conserva.
     * El versionado (snapshot) del costeo rechazado lo orquesta el caso de uso.
     */
    public void rechazar(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new BusinessRuleException("Debe indicarse el motivo del rechazo del Costeo");
        }
        transicionarA(EstadoCosteo.RECHAZADO);
        this.motivoRechazo = motivo;
    }

    /** Reabre el costeo para reproceso: RECHAZADO/COSTEADO → BORRADOR. Limpia el motivo. */
    public void reabrir() {
        transicionarA(EstadoCosteo.BORRADOR);
        this.motivoRechazo = null;
    }
}
