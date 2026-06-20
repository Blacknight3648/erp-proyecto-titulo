package com.atuan.erp.e2e.steps;

import com.atuan.erp.e2e.pages.ClientesPage;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientesSteps {

    private final ClientesPage clientes = new ClientesPage();

    @Entonces("se muestra el maestro de clientes")
    public void seMuestraMaestroClientes() {
        assertTrue(clientes.encabezadoVisible(), "No se muestra 'Gestión Maestro de Clientes'");
    }

    @Cuando("abre el modal de registrar nuevo cliente")
    public void abreModalCrearCliente() {
        clientes.abrirModalCrear();
    }

    @Entonces("se muestra el formulario de cliente")
    public void seMuestraFormularioCliente() {
        assertTrue(clientes.modalVisible(), "No se abrió el modal 'Nuevo Cliente'");
    }

    @Cuando("escribe la razón social {string}")
    public void escribeRazonSocial(String razon) {
        clientes.escribirRazonSocial(razon);
    }

    @Entonces("el botón de guardar cliente está visible")
    public void botonGuardarVisible() {
        assertTrue(clientes.botonGuardarVisible(), "No se muestra el botón 'Guardar Nuevo Cliente'");
    }
}
