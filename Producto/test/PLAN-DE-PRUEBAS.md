# Plan de Pruebas Automatizadas E2E — Frontend ERP Atuan

## 1. Objetivo

Verificar de forma automatizada que los *paths* (rutas) del frontend del ERP son
accesibles y renderizan correctamente, y que los flujos funcionales clave operan
según lo esperado, usando **Selenium + Cucumber (Gherkin)**.

## 2. Alcance

- **Smoke de navegación:** acceso a todas las rutas reales definidas en
  `App.jsx`, verificando URL y renderizado del contenido principal.
- **Autenticación:** login válido, inválido y validación de campos.
- **Flujos funcionales clave:** apertura de formularios/modales en Plantillas,
  Notas de Venta y Clientes.

Fuera de alcance: pruebas de rendimiento, compatibilidad multi-navegador
(solo Chrome), y validación profunda de cálculos de negocio.

## 3. Entorno y herramientas

- **Bajo prueba:** frontend React/Vite en `http://localhost:5173`.
- **Stack de pruebas:** Java 17, Maven, Selenium 4, Cucumber 7, JUnit 5.
- **Navegador:** Google Chrome (driver vía Selenium Manager).
- **Datos/credenciales:** autenticación en memoria `admin` / `admin`.

## 4. Estrategia

- Patrón **Page Object Model** (`pages/`) + **step definitions** (`steps/`).
- Navegación **client-side** (SPA) para preservar la sesión en memoria y cubrir
  todas las rutas (incluidas las sin enlace de menú).
- Esperas **explícitas** por condición (sin `sleep`).
- Captura de pantalla automática ante fallos, adjunta al reporte.
- Etiquetas (`@tags`) para ejecución selectiva (ver §7).

## 5. Casos de prueba (matriz de trazabilidad)

### 5.1 Autenticación — `login.feature` (`@login @smoke`)

| ID       | Escenario                              | Resultado esperado                                             |
|----------|----------------------------------------|----------------------------------------------------------------|
| LOG-01   | Login válido (`admin`/`admin`)         | Redirige a `/` y muestra el contenido principal                |
| LOG-02   | Login con credenciales incorrectas     | Muestra alerta "Credenciales incorrectas" y permanece en login |
| LOG-03   | Envío con campos vacíos                | Muestra mensajes de validación de usuario y contraseña         |

### 5.2 Smoke de navegación (`@smoke @navegacion`)

Para cada ruta: navegar → la URL coincide → el contenido principal se renderiza
(no redirige a `/login`).

| ID       | Feature                              | Rutas cubiertas                                                                                                                                 |
|----------|--------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| NAV-C    | `navegacion_comercial.feature`       | `/comercial`, `/comercial/tablero`, `/comercial/solicitudes-costos`, `/comercial/administracion-negocios`, `/comercial/gestion-plantillas`, `/comercial/gestion-proyectos`, `/comercial/ordenes-produccion`, `/registros-nv`, `/detalle-nv` |
| NAV-P    | `navegacion_produccion.feature`      | `/produccion`, `/dashboard-op`, `/produccion/tablero-op`, `/op-registro`, `/produccion/costeo-mp`, `/produccion/emitir-oc`, `/produccion/ordenes`, `/produccion/compras`, `/produccion/hoja-compra` |
| NAV-U    | `navegacion_usuarios_admin.feature`  | `/gestion-usuarios`, `/gestion-usuarios/colaboradores`, `/gestion-usuarios/clientes`, `/gestion-usuarios/proveedores`, `/gestion-usuarios/vendedores`, `/admin/datos-maestros`, `/admin/areas`, `/admin/roles` |
| NAV-T    | `navegacion_trazabilidad.feature`    | `/trazabilidad`, `/trazabilidad/completa`, `/trazabilidad/global`                                                                                |
| NAV-404  | `navegacion_trazabilidad.feature`    | Ruta inexistente (`/ruta-que-no-existe`) → redirige a `/`                                                                                        |

### 5.3 Flujos funcionales (`@funcional @requiere-backend`)

| ID       | Feature                       | Escenario                                                  | Resultado esperado                                                |
|----------|-------------------------------|------------------------------------------------------------|-------------------------------------------------------------------|
| PLA-01   | `flujo_plantillas.feature`    | La biblioteca de plantillas se muestra                     | Encabezado "Biblioteca de Plantillas" visible                     |
| PLA-02   | `flujo_plantillas.feature`    | Abrir y validar formulario de nueva prenda                 | Formulario visible; botón "Crear Plantilla" deshabilitado→habilitado al escribir el nombre |
| NV-01    | `flujo_notas_venta.feature`   | El listado de notas de venta se muestra                    | Encabezado "Gestión Notas de Venta" y buscador visibles           |
| NV-02    | `flujo_notas_venta.feature`   | Abrir el formulario de nueva nota de venta                 | Formulario "Nueva Nota de Venta" visible                          |
| CLI-01   | `flujo_clientes.feature`      | El maestro de clientes se muestra                          | Encabezado "Gestión Maestro de Clientes" visible                  |
| CLI-02   | `flujo_clientes.feature`      | Abrir el formulario de registro de cliente                 | Modal "Nuevo Cliente" visible; botón "Guardar" presente           |

## 6. Criterios de aceptación

- Todos los escenarios `@smoke` pasan con el frontend levantado.
- Los escenarios `@funcional` pasan con backend y datos disponibles.
- Cualquier fallo genera captura adjunta en `target/cucumber-report.html`.

## 7. Ejecución

| Objetivo                          | Comando                                                       |
|-----------------------------------|---------------------------------------------------------------|
| Suite completa                    | `mvn test`                                                    |
| Sin backend (smoke + login)       | `mvn test -Dcucumber.filter.tags="not @requiere-backend"`     |
| Solo smoke                        | `mvn test -Dcucumber.filter.tags="@smoke"`                    |
| Por módulo                        | `mvn test -Dcucumber.filter.tags="@comercial"`                |
| Depuración (ver navegador)        | `mvn test -Dheadless=false`                                   |

## 8. Riesgos y supuestos

- **Selectores frágiles:** la app no tiene `data-testid`; los localizadores
  dependen de textos visibles. Cambios de copy pueden requerir ajustes.
- **Dependencia de datos:** los flujos funcionales pueden variar según el estado
  del backend; por eso van etiquetados `@requiere-backend`.
- **Entorno:** requiere Chrome y conectividad para que Selenium Manager resuelva
  el driver la primera vez.
