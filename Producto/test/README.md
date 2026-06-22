# Pruebas Automatizadas E2E — Frontend ERP Atuan

Framework de pruebas **End-to-End** del frontend con **Selenium 4 + Cucumber
(Gherkin) + JUnit 5**, en Java 17 / Maven. Cubre los *paths* (rutas) del frontend
mediante pruebas de **smoke de navegación** (todas las rutas reales) y **flujos
funcionales** clave.

## Requisitos previos

| Herramienta      | Versión        | Notas                                                        |
|------------------|----------------|--------------------------------------------------------------|
| JDK              | 17 o superior  | `java -version`                                              |
| Maven            | 3.8+           | `mvn -version`                                               |
| Google Chrome    | reciente       | Selenium Manager descarga el `chromedriver` automáticamente. |

No es necesario descargar `chromedriver` manualmente: **Selenium Manager**
(incluido en Selenium 4.6+) lo resuelve solo.

## Levantar la aplicación bajo prueba

Las pruebas atacan el frontend en `http://localhost:5173`.

```bash
cd ../frontend
npm install        # solo la primera vez
npm run dev        # deja el servidor escuchando en http://localhost:5173
```

- Las pruebas de **navegación (smoke)** y de **login** funcionan solo con el
  frontend levantado (la autenticación es en memoria con `admin` / `admin`).
- Los **flujos funcionales** (`@requiere-backend`) pueden necesitar además el
  backend Spring arriba con datos cargados.

## Ejecutar las pruebas

Desde esta carpeta (`Producto/Pruebas-Automatizadas`):

```bash
# Suite completa (smoke + login + funcionales)
mvn test

# Solo lo que NO requiere backend (smoke + login)
mvn test -Dcucumber.filter.tags="not @requiere-backend"

# Solo smoke de navegación
mvn test -Dcucumber.filter.tags="@smoke"

# Solo un módulo
mvn test -Dcucumber.filter.tags="@comercial"

# Ver el navegador (desactivar headless) para depurar
mvn test -Dheadless=false
```

### Configuración

Parámetros en `src/test/resources/config.properties` (sobreescribibles con `-D`):

| Propiedad         | Por defecto              | Descripción                          |
|-------------------|--------------------------|--------------------------------------|
| `base.url`        | `http://localhost:5173`  | URL del frontend                     |
| `headless`        | `true`                   | Ejecuta Chrome sin interfaz          |
| `timeout.seconds` | `15`                     | Timeout de las esperas explícitas    |
| `login.user`      | `admin`                  | Usuario de login                     |
| `login.password`  | `admin`                  | Contraseña de login                  |

## Reportes

Tras ejecutar `mvn test`:

- `target/cucumber-report.html` — reporte HTML navegable.
- `target/cucumber.json` — reporte JSON (para integraciones/CI).

Si un escenario falla, se adjunta automáticamente una **captura de pantalla** al
reporte.

## Estructura del proyecto

```
src/test/
├── java/com/atuan/erp/e2e/
│   ├── runner/RunCucumberTest.java   # Runner JUnit 5 + Cucumber
│   ├── config/                       # Config + DriverFactory (Selenium Manager)
│   ├── hooks/Hooks.java              # Ciclo de vida del navegador + screenshots
│   ├── pages/                        # Page Objects (POM)
│   └── steps/                        # Step definitions (Gherkin ES)
└── resources/
    ├── config.properties
    ├── junit-platform.properties
    └── features/                     # Archivos .feature (Gherkin en español)
```

## Notas de diseño

- **Navegación SPA sin recargar:** la sesión vive en estado React no persistido.
  Los tests inician sesión por el formulario y navegan con
  `history.pushState` + `popstate` (ver `AppNav`), conservando la sesión y
  cubriendo todas las rutas, incluso las sin enlace en el menú.
- **Sin `data-testid`:** los selectores usan `href`, `placeholder`, `type` y
  texto visible. Se recomienda (mejora futura) agregar `data-testid` para
  robustecer los localizadores.

Ver `PLAN-DE-PRUEBAS.md` para la matriz de trazabilidad de casos.
