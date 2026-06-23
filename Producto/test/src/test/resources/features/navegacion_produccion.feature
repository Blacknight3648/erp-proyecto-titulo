# language: es
@smoke @navegacion @produccion
Característica: Navegación del módulo Producción
  Como usuario autenticado
  Quiero acceder a todas las rutas del módulo Producción
  Para verificar que cada vista carga correctamente

  Antecedentes:
    Dado que el usuario está en la página de login
    Y que el usuario ha iniciado sesión

  Esquema del escenario: Acceso a la ruta "<ruta>"
    Cuando navega a la ruta "<ruta>"
    Entonces la ruta actual es "<ruta>"
    Y se muestra el contenido principal de la aplicación

    Ejemplos:
      | ruta                      |
      | /produccion               |
      | /dashboard-op             |
      | /produccion/tablero-op    |
      | /op-registro              |
      | /produccion/costeo-mp     |
      | /produccion/emitir-oc     |
      | /produccion/ordenes       |
      | /produccion/compras       |
      | /produccion/hoja-compra   |
