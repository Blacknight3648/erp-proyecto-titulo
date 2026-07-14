package backend.com.shared.domain.enums;

/**
 * Tipo de dato de un campo de atributo dinámico de accesorio, usado para que
 * el frontend sepa qué control dibujar y de dónde sacar sus opciones.
 */
public enum TipoDatoAtributoAccesorio {
    /** Lista fija de opciones definida en {@code opciones} (ej. Terminal: Fijo/Separable). */
    LISTA,
    /** Texto libre corto. */
    TEXTO,
    /** Valor numérico. */
    NUMERO,
    /** Selección de un color del catálogo {@code color_tela} (reutiliza "Colores" de Parametría). */
    REFERENCIA_COLOR
}
