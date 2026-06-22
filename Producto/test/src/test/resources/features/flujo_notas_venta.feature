# language: es
@funcional @requiere-backend @comercial
Característica: Notas de Venta
  Como usuario del área comercial
  Quiero registrar y consultar notas de venta
  Para gestionar las ventas de la empresa

  Antecedentes:
    Dado que el usuario está en la página de login
    Y que el usuario ha iniciado sesión
    Y navega a la ruta "/registros-nv"

  Escenario: El listado de notas de venta se muestra
    Entonces se muestra el listado de notas de venta

  Escenario: Abrir el formulario de nueva nota de venta
    Cuando abre el formulario de nueva nota de venta
    Entonces se muestra el formulario de nueva nota de venta
