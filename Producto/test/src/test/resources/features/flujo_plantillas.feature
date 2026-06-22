# language: es
@funcional @requiere-backend @comercial
Característica: Gestión de Plantillas (Biblioteca de Plantillas)
  Como usuario del área comercial
  Quiero gestionar las plantillas de prendas
  Para reutilizarlas en los costeos y notas de venta

  Antecedentes:
    Dado que el usuario está en la página de login
    Y que el usuario ha iniciado sesión
    Y navega a la ruta "/comercial/gestion-plantillas"

  Escenario: La biblioteca de plantillas se muestra
    Entonces se muestra el encabezado de plantillas

  Escenario: Abrir y validar el formulario de nueva prenda
    Cuando abre el formulario de nueva prenda
    Entonces se muestra el formulario de creación de plantilla
    Y el botón de crear plantilla está deshabilitado
    Cuando escribe el nombre de prenda "MOCHILA TEST"
    Entonces el botón de crear plantilla está habilitado
