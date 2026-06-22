package com.atuan.erp.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object de la pantalla de Login.
 * La app autentica en memoria con credenciales fijas admin/admin
 * (ver AuthContext.jsx). Sin data-testid: se localiza por placeholder,
 * type de input y type=submit.
 */
public class LoginPage extends BasePage {

    private final By usuario = By.cssSelector("input[placeholder='admin']");
    private final By password = By.cssSelector("input[type='password']");
    private final By botonIngresar = By.cssSelector("button[type='submit']");

    public void escribirUsuario(String user) {
        type(usuario, user);
    }

    public void escribirPassword(String pass) {
        type(password, pass);
    }

    public void enviar() {
        click(botonIngresar);
    }

    /** Llena ambos campos y envia el formulario. */
    public void iniciarSesion(String user, String pass) {
        escribirUsuario(user);
        escribirPassword(pass);
        enviar();
    }

    public boolean estaVisible() {
        return isDisplayed(usuario) && isDisplayed(botonIngresar);
    }

    /** Devuelve el texto de un mensaje de validacion en linea si esta presente. */
    public boolean tieneMensajeValidacion(String fragmento) {
        return isDisplayed(By.xpath("//span[contains(normalize-space(.), \"" + fragmento + "\")]"));
    }

    /** Devuelve el texto del alert de credenciales y lo acepta. Null si no hay alert. */
    public String leerYAceptarAlerta() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            String texto = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            return texto;
        } catch (NoAlertPresentException | org.openqa.selenium.TimeoutException e) {
            return null;
        }
    }
}
