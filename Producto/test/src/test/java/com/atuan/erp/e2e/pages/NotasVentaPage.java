package com.atuan.erp.e2e.pages;

import org.openqa.selenium.By;

/**
 * Page Object de "Notas de Venta" (NotaDeVenta.jsx / ListaNV.jsx).
 * Encabezado: "Gestion Notas de Venta". Boton "Nueva NV Manual" abre el
 * formulario de creacion (FormularioNV).
 */
public class NotasVentaPage extends BasePage {

    private final By encabezado = By.xpath("//h1[contains(normalize-space(.), 'Notas de Venta')]");
    private final By botonNuevaNV = By.xpath("//button[contains(normalize-space(.), 'Nueva NV')]");
    private final By buscador = By.cssSelector("input[placeholder='Buscar...']");
    // Al abrir el formulario, FormularioNV muestra el h2 "Nueva Nota de Venta".
    private final By tituloFormulario = By.xpath("//h2[contains(normalize-space(.), 'Nueva Nota de Venta')]");

    public boolean encabezadoVisible() {
        return isDisplayed(encabezado);
    }

    public void abrirFormularioNuevaNV() {
        click(botonNuevaNV);
    }

    public boolean formularioAbierto() {
        return isDisplayed(tituloFormulario);
    }

    public boolean buscadorVisible() {
        return isDisplayed(buscador);
    }

    public void buscar(String termino) {
        type(buscador, termino);
    }
}
