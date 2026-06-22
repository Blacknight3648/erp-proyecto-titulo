package com.atuan.erp.e2e.steps;

import com.atuan.erp.e2e.config.Config;
import com.atuan.erp.e2e.pages.AppNav;
import com.atuan.erp.e2e.pages.LoginPage;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {

    private final LoginPage login = new LoginPage();
    private final AppNav app = new AppNav();

    @Dado("que el usuario está en la página de login")
    public void usuarioEnLogin() {
        assertTrue(login.estaVisible(), "No se muestra la pantalla de login");
    }

    @Dado("que el usuario ha iniciado sesión")
    public void usuarioAutenticado() {
        login.iniciarSesion(Config.loginUser(), Config.loginPassword());
        assertTrue(app.waitForPath("/"), "El login no redirigió a la ruta de inicio");
    }

    @Cuando("inicia sesión con usuario {string} y contraseña {string}")
    public void iniciaSesion(String usuario, String password) {
        login.iniciarSesion(usuario, password);
    }

    @Cuando("envía el formulario de login sin completar los campos")
    public void enviaFormularioVacio() {
        login.enviar();
    }

    @Entonces("es redirigido a la ruta {string}")
    public void esRedirigidoA(String ruta) {
        assertTrue(app.waitForPath(ruta), "No se redirigió a " + ruta + ", ruta actual: " + app.currentPath());
    }

    @Entonces("permanece en la ruta {string}")
    public void permaneceEn(String ruta) {
        assertEquals(ruta, app.currentPath(), "La ruta cambió de forma inesperada");
    }

    @Entonces("se muestra una alerta con el texto {string}")
    public void seMuestraAlerta(String fragmento) {
        String texto = login.leerYAceptarAlerta();
        assertNotNull(texto, "No apareció ninguna alerta");
        assertTrue(texto.contains(fragmento), "La alerta no contiene '" + fragmento + "'. Texto: " + texto);
    }

    @Entonces("se muestra el mensaje de validación {string}")
    public void seMuestraMensajeValidacion(String mensaje) {
        assertTrue(login.tieneMensajeValidacion(mensaje), "No se mostró el mensaje de validación: " + mensaje);
    }
}
