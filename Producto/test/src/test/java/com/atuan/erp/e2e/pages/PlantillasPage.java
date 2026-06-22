package com.atuan.erp.e2e.pages;

import org.openqa.selenium.By;

/**
 * Page Object de "Gestion de Plantillas" (GestionPlantillas.jsx).
 * Encabezado: "Biblioteca de Plantillas". Boton "Nueva Prenda" abre el
 * formulario con un input de nombre y el boton "Crear Plantilla".
 */
public class PlantillasPage extends BasePage {

    private final By encabezado = By.xpath("//h1[contains(normalize-space(.), 'Biblioteca de Plantillas')]");
    private final By botonNuevaPrenda = By.xpath("//button[contains(normalize-space(.), 'Nueva Prenda')]");
    private final By inputNombre = By.cssSelector("input[placeholder^='Nombre de la prenda']");
    private final By botonCrear = By.xpath("//button[contains(normalize-space(.), 'Crear Plantilla')]");

    public boolean encabezadoVisible() {
        return isDisplayed(encabezado);
    }

    public void abrirFormularioNuevaPrenda() {
        click(botonNuevaPrenda);
    }

    public boolean formularioVisible() {
        return isDisplayed(inputNombre) && isDisplayed(botonCrear);
    }

    public void escribirNombre(String nombre) {
        type(inputNombre, nombre);
    }

    /** El boton "Crear Plantilla" esta deshabilitado mientras el nombre este vacio. */
    public boolean botonCrearHabilitado() {
        return waitVisible(botonCrear).isEnabled();
    }
}
