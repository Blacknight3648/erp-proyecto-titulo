package com.atuan.erp.e2e.pages;

import org.openqa.selenium.By;

/**
 * Page Object de "Gestion de Clientes" (GestionClientes.jsx + ClienteModal.jsx).
 * Encabezado: "Gestion Maestro de Clientes". El boton "Registrar Nuevo Cliente"
 * abre un modal con titulo "Nuevo Cliente" y un formulario.
 */
public class ClientesPage extends BasePage {

    private final By encabezado = By.xpath("//h1[contains(normalize-space(.), 'Maestro de Clientes')]");
    private final By botonRegistrar = By.xpath("//button[contains(normalize-space(.), 'Registrar Nuevo Cliente')]");
    private final By tituloModal = By.xpath("//h2[contains(normalize-space(.), 'Nuevo Cliente')]");
    private final By inputRazonSocial = By.cssSelector("input[name='razonSocial']");
    private final By botonGuardar = By.xpath("//button[@type='submit' and contains(normalize-space(.), 'Guardar')]");
    private final By buscador = By.cssSelector("input[placeholder^='Buscar por raz']");

    public boolean encabezadoVisible() {
        return isDisplayed(encabezado);
    }

    public void abrirModalCrear() {
        click(botonRegistrar);
    }

    public boolean modalVisible() {
        return isDisplayed(tituloModal) && isDisplayed(inputRazonSocial);
    }

    public void escribirRazonSocial(String razon) {
        type(inputRazonSocial, razon);
    }

    public boolean botonGuardarVisible() {
        return isDisplayed(botonGuardar);
    }

    public boolean buscadorVisible() {
        return isDisplayed(buscador);
    }
}
