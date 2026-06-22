package com.atuan.erp.e2e.hooks;

import com.atuan.erp.e2e.config.Config;
import com.atuan.erp.e2e.config.DriverFactory;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Ciclo de vida del navegador por escenario:
 * abre el driver y la pantalla de login antes de cada escenario,
 * adjunta una captura si falla y cierra el navegador al terminar.
 */
public class Hooks {

    @Before
    public void abrirNavegador() {
        WebDriver driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
        // Punto de partida: pantalla de login (la sesion vive en memoria).
        driver.get(Config.baseUrl() + "/login");
    }

    @After
    public void cerrarNavegador(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        if (scenario.isFailed() && driver instanceof TakesScreenshot) {
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(png, "image/png", "captura-fallo");
        }
        DriverFactory.quitDriver();
    }
}
