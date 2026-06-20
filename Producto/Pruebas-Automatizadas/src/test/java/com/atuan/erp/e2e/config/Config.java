package com.atuan.erp.e2e.config;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

/**
 * Carga y expone la configuracion de las pruebas desde {@code config.properties}.
 * Cualquier propiedad puede sobreescribirse por -D en la linea de comandos,
 * por ejemplo: {@code mvn test -Dheadless=false}.
 */
public final class Config {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("No se encontro config.properties en el classpath");
            }
            PROPS.load(in);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Config() {
    }

    private static String get(String key) {
        // Prioridad: -Dkey=valor (System property) sobre el archivo.
        return System.getProperty(key, PROPS.getProperty(key));
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static Duration timeout() {
        return Duration.ofSeconds(Long.parseLong(get("timeout.seconds")));
    }

    public static String loginUser() {
        return get("login.user");
    }

    public static String loginPassword() {
        return get("login.password");
    }
}
