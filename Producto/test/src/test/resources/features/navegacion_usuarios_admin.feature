# language: es
@smoke @navegacion @usuarios @admin
Característica: Navegación de Gestión de Usuarios y Administración
  Como usuario autenticado
  Quiero acceder a las rutas de gestión de usuarios y administración
  Para verificar que cada vista carga correctamente

  Antecedentes:
    Dado que el usuario está en la página de login
    Y que el usuario ha iniciado sesión

  Esquema del escenario: Acceso a la ruta "<ruta>"
    Cuando navega a la ruta "<ruta>"
    Entonces la ruta actual es "<ruta>"
    Y se muestra el contenido principal de la aplicación

    Ejemplos:
      | ruta                            |
      | /gestion-usuarios               |
      | /gestion-usuarios/colaboradores |
      | /gestion-usuarios/clientes      |
      | /gestion-usuarios/proveedores   |
      | /gestion-usuarios/vendedores    |
      | /admin/datos-maestros           |
      | /admin/areas                    |
      | /admin/roles                    |
