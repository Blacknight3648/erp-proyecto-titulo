# language: es
@login @smoke
Característica: Autenticación en el sistema
  Como usuario del ERP
  Quiero iniciar sesión con mis credenciales
  Para acceder a los módulos protegidos de la aplicación

  Antecedentes:
    Dado que el usuario está en la página de login

  @login-valido
  Escenario: Inicio de sesión válido redirige al inicio
    Cuando inicia sesión con usuario "admin" y contraseña "admin"
    Entonces es redirigido a la ruta "/"
    Y se muestra el contenido principal de la aplicación

  @login-invalido
  Escenario: Inicio de sesión con credenciales incorrectas
    Cuando inicia sesión con usuario "usuario" y contraseña "incorrecta"
    Entonces se muestra una alerta con el texto "Credenciales incorrectas"
    Y permanece en la ruta "/login"

  @login-validacion
  Escenario: Validación de campos obligatorios
    Cuando envía el formulario de login sin completar los campos
    Entonces se muestra el mensaje de validación "Introduce un usuario o correo electrónico"
    Y se muestra el mensaje de validación "Introduce una contraseña"
