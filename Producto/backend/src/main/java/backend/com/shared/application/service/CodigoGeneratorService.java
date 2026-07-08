package backend.com.shared.application.service;

import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 * Genera los códigos de negocio (SKU de artículos, códigos de catálogos
 * paramétricos) a partir de los datos ingresados por el usuario, para que
 * nunca deba escribirlos a mano.
 *
 * <p>
 * Cada método recibe un {@link Predicate} que indica si un código candidato
 * ya existe en el catálogo correspondiente; ante una colisión se prueba una
 * variante más específica y, en última instancia, se agrega un sufijo
 * numérico incremental para garantizar unicidad.
 * </p>
 */
public interface CodigoGeneratorService {

    /**
     * Código por abreviatura de texto (Familias, Colores, Atributos,
     * Categorías de Tela). Ej: {@code generarPorAbreviatura("T-", "Gabardina", ...)} → {@code "T-GAB"}.
     */
    String generarPorAbreviatura(String prefijo, String textoBase, Predicate<String> existeCodigo);

    /**
     * Código de Composición: concatena los porcentajes presentes en la
     * descripción (ej. "60% Algodón / 40% Poliéster" → "C-6040"); ante
     * colisión de patrón de porcentaje agrega la abreviatura del material
     * dominante (ej. "C-8020AL" vs "C-8020PO").
     */
    String generarComposicion(String prefijo, String descripcionComposicion, Predicate<String> existeCodigo);

    /** Código de Gramaje a partir del valor en gr/m² (ej. {@code "G-100"}). */
    String generarGramaje(String prefijo, BigDecimal valorGramosM2, Predicate<String> existeCodigo);

    /** Correlativo secuencial por prefijo de negocio (ej. {@code "CIE-001"}, {@code "GAB-002"}). */
    String siguienteCorrelativo(String prefijo, Predicate<String> existeCodigo);
}
