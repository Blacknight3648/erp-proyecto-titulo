package com.atuan.erp.e2e.steps;

import com.atuan.erp.e2e.pages.AppNav;
import com.atuan.erp.e2e.pages.SidebarNav;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pasos de navegacion. Nota: Cucumber empareja por la expresion del paso,
 * sin importar el keyword (Cuando/Y/Entonces), por lo que cada expresion se
 * define una sola vez.
 */
public class NavigationSteps {

    private final AppNav app = new AppNav();
    private final SidebarNav sidebar = new SidebarNav();

    @Cuando("navega a la ruta {string}")
    public void navegaA(String ruta) {
        app.irA(ruta);
    }

    @Entonces("la ruta actual es {string}")
    public void laRutaActualEs(String ruta) {
        assertTrue(app.waitForPath(ruta),
                "Se esperaba la ruta " + ruta + " pero la actual es " + app.currentPath());
    }

    @Entonces("se muestra el contenido principal de la aplicación")
    public void seMuestraContenidoPrincipal() {
        assertTrue(app.contenidoPrincipalVisible(),
                "No se renderizó el contenido principal (¿redirigido a /login?)");
    }

    @Entonces("se muestra algún encabezado en la página")
    public void seMuestraEncabezado() {
        assertTrue(app.tieneEncabezado(), "No se encontró ningún encabezado visible");
    }

    // --- Navegación REAL por el menú lateral ---

    @Cuando("abre el módulo {string} en el menú lateral")
    public void abreModulo(String etiqueta) {
        sidebar.abrirModulo(etiqueta);
    }

    @Cuando("hace clic en el enlace del menú {string}")
    public void clickEnlaceMenu(String href) {
        sidebar.clickEnlace(href);
    }
}
