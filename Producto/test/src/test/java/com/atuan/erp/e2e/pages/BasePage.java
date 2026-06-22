package com.atuan.erp.e2e.pages;

import com.atuan.erp.e2e.config.Config;
import com.atuan.erp.e2e.config.DriverFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base para los Page Objects: expone el WebDriver, una espera explicita
 * configurada y helpers comunes (click, type, lectura de texto, URL).
 * No usa Thread.sleep: toda espera es explicita por condicion.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Config.timeout());
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        WebElement el = waitClickable(locator);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            // Algunos elementos tienen overlays decorativos (ej. el boton de login);
            // se usa un click via JavaScript como respaldo.
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    protected void type(By locator, String text) {
        WebElement el = waitVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected String text(By locator) {
        return waitVisible(locator).getText();
    }

    public boolean isDisplayed(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Ruta actual (path) de la URL del navegador, sin host. */
    public String currentPath() {
        return (String) ((JavascriptExecutor) driver)
                .executeScript("return window.location.pathname;");
    }

    /** Espera hasta que la ruta actual sea exactamente la indicada. */
    public boolean waitForPath(String path) {
        return wait.until(d -> path.equals(currentPath()));
    }
}
