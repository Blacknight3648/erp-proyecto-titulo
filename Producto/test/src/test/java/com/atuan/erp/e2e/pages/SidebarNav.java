package com.atuan.erp.e2e.pages;

import org.openqa.selenium.By;

/**
 * Page Object del menu lateral (Sidebar.jsx).
 * Permite validar la navegacion REAL por clics del usuario: abrir un modulo
 * (boton con texto) y seguir un enlace de submenu (NavLink -> a[href]).
 */
public class SidebarNav extends BasePage {

    /** Abre un modulo del menu por su etiqueta visible (ej: "Area Comercial"). */
    public void abrirModulo(String etiqueta) {
        // Los acentos del codigo usan caracteres especiales; se usa contains por robustez.
        By boton = By.xpath("//button[.//span[contains(normalize-space(.), \"" + etiqueta + "\")]]");
        click(boton);
    }

    /** Hace clic en un enlace del menu por su href (ruta). */
    public void clickEnlace(String href) {
        click(By.cssSelector("a[href='" + href + "']"));
    }

    public boolean existeEnlace(String href) {
        return isDisplayed(By.cssSelector("a[href='" + href + "']"));
    }
}
