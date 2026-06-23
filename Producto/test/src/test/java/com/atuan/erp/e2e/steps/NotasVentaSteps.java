package com.atuan.erp.e2e.steps;

import com.atuan.erp.e2e.pages.NotasVentaPage;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotasVentaSteps {

    private final NotasVentaPage notas = new NotasVentaPage();

    @Entonces("se muestra el listado de notas de venta")
    public void seMuestraListadoNV() {
        assertTrue(notas.encabezadoVisible(), "No se muestra 'Gestión Notas de Venta'");
        assertTrue(notas.buscadorVisible(), "No se muestra el buscador del listado");
    }

    @Cuando("abre el formulario de nueva nota de venta")
    public void abreFormularioNuevaNV() {
        notas.abrirFormularioNuevaNV();
    }

    @Entonces("se muestra el formulario de nueva nota de venta")
    public void seMuestraFormularioNV() {
        assertTrue(notas.formularioAbierto(), "No se abrió el formulario 'Nueva Nota de Venta'");
    }
}
