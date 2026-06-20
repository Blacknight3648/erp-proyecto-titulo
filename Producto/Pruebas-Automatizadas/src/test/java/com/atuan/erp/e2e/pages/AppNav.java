package com.atuan.erp.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Navegacion dentro de la SPA (React Router) sin recargar la pagina.
 *
 * La autenticacion del frontend vive en estado React no persistido: un
 * driver.get(ruta) recargaria y perderia la sesion (redirigiendo a /login).
 * Por eso navegamos via history.pushState + un evento popstate sintetico,
 * que React Router escucha y procesa como navegacion client-side,
 * conservando la sesion. Asi se cubren TODAS las rutas, incluso las que
 * no tienen enlace en el menu lateral.
 */
public class AppNav extends BasePage {

    /** Navega client-side a la ruta indicada. */
    public void irA(String path) {
        ((JavascriptExecutor) driver).executeScript(
                "window.history.pushState({}, '', arguments[0]);" +
                "window.dispatchEvent(new PopStateEvent('popstate'));",
                path);
    }

    public boolean estaEnLogin() {
        return "/login".equals(currentPath());
    }

    /** Verdadero si el contenido principal de la app se renderizo (no es login). */
    public boolean contenidoPrincipalVisible() {
        return isDisplayed(By.cssSelector("main")) && !estaEnLogin();
    }

    /** Verdadero si existe algun encabezado visible (h1/h2) en la pagina. */
    public boolean tieneEncabezado() {
        return isDisplayed(By.cssSelector("main h1, main h2, h1, h2"));
    }
}
