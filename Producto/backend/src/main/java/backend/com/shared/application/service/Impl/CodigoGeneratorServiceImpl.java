package backend.com.shared.application.service.impl;

import backend.com.shared.application.service.CodigoGeneratorService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CodigoGeneratorServiceImpl implements CodigoGeneratorService {

    private static final Set<String> PALABRAS_VACIAS = Set.of(
            "DE", "LA", "EL", "LOS", "LAS", "Y", "CON", "PARA", "A", "EN", "SIN");
    private static final Pattern PORCENTAJE = Pattern.compile("(\\d{1,3})\\s*%");

    @Override
    public String generarPorAbreviatura(String prefijo, String textoBase, Predicate<String> existeCodigo) {
        String candidato = prefijo + abreviar(textoBase, 3);
        if (!existeCodigo.test(candidato)) return candidato;

        for (int longitud = 4; longitud <= 6; longitud++) {
            candidato = prefijo + abreviar(textoBase, longitud);
            if (!existeCodigo.test(candidato)) return candidato;
        }
        return conSufijoNumerico(prefijo + abreviar(textoBase, 3), existeCodigo);
    }

    @Override
    public String generarComposicion(String prefijo, String descripcionComposicion, Predicate<String> existeCodigo) {
        String texto = descripcionComposicion == null ? "" : descripcionComposicion;
        Matcher m = PORCENTAJE.matcher(texto);
        StringBuilder porcentajes = new StringBuilder();
        while (m.find()) {
            porcentajes.append(String.format("%02d", Integer.parseInt(m.group(1))));
        }

        String base = porcentajes.length() > 0 ? prefijo + porcentajes : prefijo + abreviar(texto, 4);
        if (!existeCodigo.test(base)) return base;

        String primerMaterial = Arrays.stream(texto.split("[\\s/]+"))
                .filter(palabra -> !palabra.isBlank() && !palabra.matches("\\d{1,3}%?"))
                .findFirst().orElse("");
        String candidatoDesambiguado = base + abreviar(primerMaterial, 2);
        if (!primerMaterial.isBlank() && !existeCodigo.test(candidatoDesambiguado)) {
            return candidatoDesambiguado;
        }
        return conSufijoNumerico(base, existeCodigo);
    }

    @Override
    public String generarGramaje(String prefijo, BigDecimal valorGramosM2, Predicate<String> existeCodigo) {
        BigDecimal limpio = valorGramosM2.stripTrailingZeros();
        String valor = limpio.scale() <= 0 ? limpio.toBigInteger().toString() : limpio.toPlainString();
        String base = prefijo + valor;
        return existeCodigo.test(base) ? conSufijoNumerico(base, existeCodigo) : base;
    }

    @Override
    public String siguienteCorrelativo(String prefijo, Predicate<String> existeCodigo) {
        long n = 1;
        String candidato;
        do {
            candidato = prefijo + "-" + String.format("%03d", n++);
        } while (existeCodigo.test(candidato));
        return candidato;
    }

    private String conSufijoNumerico(String base, Predicate<String> existeCodigo) {
        long n = 2;
        String candidato;
        do {
            candidato = base + n++;
        } while (existeCodigo.test(candidato));
        return candidato;
    }

    private String abreviar(String texto, int longitud) {
        if (texto == null || texto.isBlank()) return "X";
        String limpio = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase()
                .replaceAll("[^A-Z0-9 ]", "");
        String significativo = Arrays.stream(limpio.split("\\s+"))
                .filter(palabra -> !palabra.isBlank() && !PALABRAS_VACIAS.contains(palabra))
                .collect(Collectors.joining());
        if (significativo.isBlank()) {
            significativo = limpio.replaceAll("\\s+", "");
        }
        if (significativo.isBlank()) return "X";
        return significativo.length() <= longitud ? significativo : significativo.substring(0, longitud);
    }
}
