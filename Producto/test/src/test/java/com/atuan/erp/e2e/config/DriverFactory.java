package com.atuan.erp.e2e.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Mantiene una instancia de WebDriver por hilo y la crea bajo demanda.
 * Usa Selenium Manager (incluido en Selenium 4.6+) para resolver el chromedriver,
 * por lo que no requiere descargar binarios manualmente.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        if (DRIVER.get() == null) {
            DRIVER.set(create());
        }
        return DRIVER.get();
    }

    private static WebDriver create() {
        ChromeOptions options = new ChromeOptions();
        if (Config.headless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,900");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        // Idioma espanol para que coincidan textos/locale de la app
        options.addArguments("--lang=es-CL");
        return new ChromeDriver(options);
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
