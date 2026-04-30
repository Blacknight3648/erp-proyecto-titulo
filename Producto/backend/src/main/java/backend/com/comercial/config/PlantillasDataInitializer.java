package backend.com.comercial.config;

import backend.com.comercial.application.dto.ConfiguracionPlantillaDTO;
import backend.com.comercial.application.service.ConfiguracionPlantillaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Inicializa la configuración de plantillas predeterminadas en la base de datos
 * si aún no existen. Estos valores reemplazan la lógica hardcoded que estaba en
 * el frontend (PlantillasPanel.jsx -> PLANTILLA_CONFIG.keywords).
 */
@Component
@RequiredArgsConstructor
public class PlantillasDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PlantillasDataInitializer.class);

    private final ConfiguracionPlantillaService service;

    @Override
    public void run(ApplicationArguments args) {
        List<ConfiguracionPlantillaDTO> existentes = service.findAll();
        if (!existentes.isEmpty()) {
            log.info("Configuraciones de plantillas ya inicializadas ({} registros). Saltando seed.",
                    existentes.size());
            return;
        }

        log.info("Inicializando configuraciones de plantillas predeterminadas...");

        save("POLERA", Set.of("nombrePrenda", "tela", "composicion", "color", "cuello", "mangas", "peso", "obsModelo"));
        save("POLERÓN", Set.of("nombrePrenda", "tela", "composicion", "color", "gorro", "bolsillos", "relleno",
                "pretinasRuedo", "abotonaduraCierre", "obsModelo"));
        save("PANTALÓN", Set.of("nombrePrenda", "tela", "composicion", "color", "fuelles", "bolsillos", "pretinasRuedo",
                "abotonaduraCierre", "cortesAplicaciones", "obsModelo"));
        save("CAMISA", Set.of("nombrePrenda", "tela", "composicion", "color", "cuello", "mangas", "abotonaduraCierre",
                "bolsillos", "obsModelo"));
        save("SHORT", Set.of("nombrePrenda", "tela", "composicion", "color", "pretinasRuedo", "bolsillos", "fuelles",
                "obsModelo"));
        save("CHAQUETA", Set.of("nombrePrenda", "tela", "composicion", "color", "forro", "colorForro", "relleno",
                "gorro", "cuello", "abotonaduraCierre", "bolsillos", "mangas", "obsModelo"));
        save("CALZA", Set.of("nombrePrenda", "tela", "composicion", "color", "pretinasRuedo", "cortesAplicaciones",
                "obsModelo"));

        log.info("Configuraciones de plantillas inicializadas correctamente.");
    }

    private void save(String nombre, Set<String> campos) {
        try {
            service.save(new ConfiguracionPlantillaDTO(null, nombre, campos, null, new java.util.ArrayList<>(),
                    new java.util.ArrayList<>()));
        } catch (Exception e) {
            log.warn("No se pudo guardar configuración para '{}': {}", nombre, e.getMessage());
        }
    }
}
