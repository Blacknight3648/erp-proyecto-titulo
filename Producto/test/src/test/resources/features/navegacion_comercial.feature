# language: es
@smoke @navegacion @comercial
Característica: Navegación del módulo Comercial
  Como usuario autenticado
  Quiero acceder a todas las rutas del módulo Comercial
  Para verificar que cada vista carga correctamente

  Antecedentes:
    Dado que el usuario está en la página de login
    Y que el usuario ha iniciado sesión

  Esquema del escenario: Acceso a la ruta "<ruta>"
    Cuando navega a la ruta "<ruta>"
    Entonces la ruta actual es "<ruta>"
    Y se muestra el contenido principal de la aplicación

    Ejemplos:
      | ruta                                  |
      | /comercial                            |
      | /comercial/tablero                    |
      | /comercial/solicitudes-costos         |
      | /comercial/administracion-negocios    |
      | /comercial/gestion-plantillas         |
      | /comercial/gestion-proyectos          |
      | /comercial/ordenes-produccion         |
      | /registros-nv                         |
      | /detalle-nv                           |
