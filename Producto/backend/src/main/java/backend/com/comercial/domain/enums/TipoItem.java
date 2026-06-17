package backend.com.comercial.domain.enums;

/**
 * Clasificación del ítem dentro de una NV o EVN.
 * El nombre del valor se persiste literalmente en la BD (EnumType.STRING),
 * por lo que renombrar cualquier constante requiere migración de datos.
 */
public enum TipoItem {
    /** Orden de Producción — ítem que requiere fabricación interna. */
    OP,
    /** Solicitud de Compra — ítem que se adquiere a proveedor local. */
    SC,
    /** Solicitud de Compra por Importación — ítem importado. */
    SCI
}
