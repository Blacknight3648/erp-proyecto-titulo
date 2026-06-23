package com.atuan.erp.e2e.steps;

import com.atuan.erp.e2e.pages.PlantillasPage;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlantillasSteps {

    private final PlantillasPage plantillas = new PlantillasPage();

    @Entonces("se muestra el encabezado de plantillas")
    public void seMuestraEncabezadoPlantillas() {
        assertTrue(plantillas.encabezadoVisible(), "No se muestra 'Biblioteca de Plantillas'");
    }

    @Cuando("abre el formulario de nueva prenda")
    public void abreFormularioNuevaPrenda() {
        plantillas.abrirFormularioNuevaPrenda();
    }

    @Entonces("se muestra el formulario de creación de plantilla")
    public void seMuestraFormularioPlantilla() {
        assertTrue(plantillas.formularioVisible(), "No se muestra el formulario de nueva prenda");
    }

    @Entonces("el botón de crear plantilla está deshabilitado")
    public void botonCrearDeshabilitado() {
        assertFalse(plantillas.botonCrearHabilitado(), "El botón 'Crear Plantilla' debería estar deshabilitado");
    }

    @Cuando("escribe el nombre de prenda {string}")
    public void escribeNombrePrenda(String nombre) {
        plantillas.escribirNombre(nombre);
    }

    @Entonces("el botón de crear plantilla está habilitado")
    public void botonCrearHabilitado() {
        assertTrue(plantillas.botonCrearHabilitado(), "El botón 'Crear Plantilla' debería estar habilitado");
    }
}
