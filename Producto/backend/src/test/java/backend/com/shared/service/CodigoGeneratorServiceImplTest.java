package backend.com.shared.service;

import backend.com.shared.application.service.impl.CodigoGeneratorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CodigoGeneratorServiceImpl")
class CodigoGeneratorServiceImplTest {

    private final CodigoGeneratorServiceImpl generador = new CodigoGeneratorServiceImpl();

    private Predicate<String> ningunoExiste() {
        return codigo -> false;
    }

    // ---------------- generarPorAbreviatura ----------------

    @Test
    @DisplayName("genera la abreviatura de 3 letras del texto cuando no hay colisión")
    void generarPorAbreviatura_sinColision() {
        String codigo = generador.generarPorAbreviatura("T-", "Gabardina", ningunoExiste());

        assertThat(codigo).isEqualTo("T-GAB");
    }

    @Test
    @DisplayName("ignora tildes y usa mayúsculas")
    void generarPorAbreviatura_normalizaTexto() {
        String codigo = generador.generarPorAbreviatura("A-", "Ignífugo", ningunoExiste());

        assertThat(codigo).isEqualTo("A-IGN");
    }

    @Test
    @DisplayName("ante colisión de 3 letras prueba abreviaturas más largas")
    void generarPorAbreviatura_colisionProbaMasLargo() {
        // "T-GAB" ya existe -> debe intentar "T-GABA"
        Predicate<String> existe = codigo -> codigo.equals("T-GAB");

        String codigo = generador.generarPorAbreviatura("T-", "Gabardina", existe);

        assertThat(codigo).isEqualTo("T-GABA");
    }

    @Test
    @DisplayName("si todas las abreviaturas colisionan agrega un sufijo numérico")
    void generarPorAbreviatura_fallbackNumerico() {
        Predicate<String> existeSiempreParaAbreviaturas = codigo -> codigo.startsWith("T-GAB") && !codigo.matches(".*\\d$");

        String codigo = generador.generarPorAbreviatura("T-", "Gabardina", existeSiempreParaAbreviaturas);

        assertThat(codigo).isEqualTo("T-GAB2");
    }

    // ---------------- generarComposicion ----------------

    @Test
    @DisplayName("concatena los porcentajes de la descripción")
    void generarComposicion_concatenaPorcentajes() {
        String codigo = generador.generarComposicion("C-", "60% Algodón / 40% Poliéster", ningunoExiste());

        assertThat(codigo).isEqualTo("C-6040");
    }

    @Test
    @DisplayName("ante colisión de porcentajes agrega la abreviatura del material dominante")
    void generarComposicion_colisionAgregaMaterial() {
        Predicate<String> existe = codigo -> codigo.equals("C-8020");

        String codigo = generador.generarComposicion("C-", "80% Algodón / 20% Poliéster", existe);

        assertThat(codigo).isEqualTo("C-8020AL");
    }

    @Test
    @DisplayName("sin porcentajes cae a la abreviatura del texto")
    void generarComposicion_sinPorcentajes() {
        String codigo = generador.generarComposicion("C-", "Fibra Hueca Siliconada", ningunoExiste());

        assertThat(codigo).isEqualTo("C-FIBR");
    }

    // ---------------- generarGramaje ----------------

    @Test
    @DisplayName("usa el valor entero del gramaje cuando no tiene decimales")
    void generarGramaje_valorEntero() {
        String codigo = generador.generarGramaje("G-", new BigDecimal("100.00"), ningunoExiste());

        assertThat(codigo).isEqualTo("G-100");
    }

    @Test
    @DisplayName("conserva decimales cuando el valor no es entero")
    void generarGramaje_conDecimales() {
        String codigo = generador.generarGramaje("G-", new BigDecimal("115.50"), ningunoExiste());

        assertThat(codigo).isEqualTo("G-115.5");
    }

    // ---------------- siguienteCorrelativo ----------------

    @Test
    @DisplayName("genera el primer correlativo cuando no hay ninguno previo")
    void siguienteCorrelativo_primero() {
        String codigo = generador.siguienteCorrelativo("CIE", ningunoExiste());

        assertThat(codigo).isEqualTo("CIE-001");
    }

    @Test
    @DisplayName("salta los correlativos ya usados")
    void siguienteCorrelativo_saltaExistentes() {
        Predicate<String> existe = codigo -> codigo.equals("CIE-001") || codigo.equals("CIE-002");

        String codigo = generador.siguienteCorrelativo("CIE", existe);

        assertThat(codigo).isEqualTo("CIE-003");
    }
}
