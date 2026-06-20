# language: es
@funcional @requiere-backend @usuarios
Característica: Gestión de Clientes
  Como usuario encargado de cuentas
  Quiero registrar y consultar clientes
  Para mantener el directorio central de cuentas

  Antecedentes:
    Dado que el usuario está en la página de login
    Y que el usuario ha iniciado sesión
    Y navega a la ruta "/gestion-usuarios/clientes"

  Escenario: El maestro de clientes se muestra
    Entonces se muestra el maestro de clientes

  Escenario: Abrir el formulario de registro de cliente
    Cuando abre el modal de registrar nuevo cliente
    Entonces se muestra el formulario de cliente
    Cuando escribe la razón social "Comercial Pruebas SpA"
    Entonces el botón de guardar cliente está visible
