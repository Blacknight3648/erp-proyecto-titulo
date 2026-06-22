# language: es
@smoke @navegacion @trazabilidad
Característica: Navegación del módulo Trazabilidad y rutas inválidas
  Como usuario autenticado
  Quiero acceder a las rutas de trazabilidad
  Y verificar el manejo de rutas inexistentes

  Antecedentes:
    Dado que el usuario está en la página de login
    Y que el usuario ha iniciado sesión

  Esquema del escenario: Acceso a la ruta "<ruta>"
    Cuando navega a la ruta "<ruta>"
    Entonces la ruta actual es "<ruta>"
    Y se muestra el contenido principal de la aplicación

    Ejemplos:
      | ruta                    |
      | /trazabilidad           |
      | /trazabilidad/completa  |
      | /trazabilidad/global    |

  @ruta-invalida
  Escenario: Una ruta inexistente redirige al inicio
    Cuando navega a la ruta "/ruta-que-no-existe"
    Entonces la ruta actual es "/"
    Y se muestra el contenido principal de la aplicación
