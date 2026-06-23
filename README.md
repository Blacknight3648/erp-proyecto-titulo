# ERP Antuan Gestión

## Descripción
El ERP Antuan Gestión es un sistema integral diseñado para automatizar y optimizar los procesos operativos y comerciales de la empresa de confecciones. Resuelve problemas comunes como la fragmentación de la información, el seguimiento manual de costeo y ventas, y la desconexión entre áreas. Está dirigido a los administradores, vendedores y personal de producción de la empresa, permitiéndoles tener una trazabilidad completa desde la cotización inicial (SCOS), pasando por la Evaluación de Negocios (EVN) y la Nota de Venta (NV), hasta las órdenes de producción.

## Tecnologías utilizadas
- **Frontend**: React (JS/JSX), Vite, TailwindCSS, Lucide-React.
- **Backend**: Java 21, Spring Boot, Spring Data JPA, Spring Web.
- **Base de Datos**: MySQL (relacional) con migraciones iniciales a través de `data.sql`.
- **Otros**: Lombok, Swagger/OpenAPI, Test de postman (para documentación de la API).

## Requisitos previos
Para poder ejecutar este proyecto localmente, necesitas tener instalados los siguientes componentes:
- Node.js (v18 o superior)
- npm o yarn
- Java Development Kit (JDK 21)
- Maven (si no se utiliza el wrapper `mvnw`)

## Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd erp-proyecto-titulo
   ```

2. **Instalación del Backend**
   ```bash
   cd Producto/backend
   # Instalar dependencias usando Maven
   mvn clean install
   ```

3. **Instalación del Frontend**
   ```bash
   cd ../frontend
   # Instalar paquetes de npm
   npm install
   ```

## Configuración

### Variables de entorno del Backend
El backend está configurado principalmente en el archivo `application.properties` (`Producto/backend/src/main/resources/application.properties`). No requiere variables de entorno adicionales por defecto, ya que usa H2:
```properties
spring.datasource.url=jdbc:h2:mem:antuandb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=sa
```

### Variables de entorno del Frontend
Si existen variables como la URL de la API, se configuran en el `.env` del frontend.
```env
VITE_API_URL=http://localhost:8080/api/v1
```

## Uso / Ejecución

1. **Levantar el Backend**
   Abre una terminal en `Producto/backend`:
   ```bash
   mvn spring-boot:run
   ```
   El backend correrá en `http://localhost:8080`.

2. **Levantar el Frontend**
   Abre otra terminal en `Producto/frontend`:
   ```bash
   npm run dev
   ```
   El frontend correrá en el puerto que te indique Vite (por lo general `http://localhost:5173`).

## Arquitectura del proyecto
El proyecto sigue una arquitectura de capas:
- **Frontend (Cliente)**: Aplicación SPA desarrollada en React. Organizada modularmente por funcionalidades (comercial, inventario, etc.) dentro de `src/components/pages`. Se utiliza un patrón de hooks (ej. `useComercial`) para extraer la lógica de estado y peticiones.
- **Backend (Servidor)**: Aplicación Spring Boot que sigue la Arquitectura Hexagonal / Diseño Dirigido por Dominio (DDD).
  - **Dominio**: Modelos principales como `EvaluacionNegocio`, `NotaVenta`, etc.
  - **Aplicación**: Casos de uso (`CrearEVNUseCase`, `ActualizarEVNUseCase`).
  - **Infraestructura**: Controladores REST (`EvaluacionNegocioController`), repositorios JPA y configuración de base de datos.

## Base de datos
El sistema emplea un modelo relacional gestionado por MySQL. Las tablas principales incluyen:
- `evaluaciones_negocio` (EVN)
- `solicitudes_costos` (SCOS)
- `notas_venta` (NV)
- `ordenes_produccion` (OP)
- Tablas de relación de gastos y detalles de ítems.

La estructura inicial está definida en el archivo `data.sql` que hace seeding de las tablas necesarias para las pruebas.

## Documentación de la API
El proyecto utiliza Swagger UI para exponer la documentación de la API en vivo.
Una vez levantado el servidor backend, se puede acceder a la documentación interactiva en:
`http://localhost:8080/swagger-ui/index.html`

**Ejemplos de Endpoints Principales**:
- `GET /api/v1/comercial/evaluaciones-negocio`: Lista las evaluaciones.
- `POST /api/v1/comercial/evaluaciones-negocio`: Crea una nueva EVN.
- `PUT /api/v1/comercial/evaluaciones-negocio/{id}`: Actualiza una EVN existente.

**Ejemplo de Request (Crear EVN)**:
```json
{
  "clienteId": 1,
  "vendedorId": 1,
  "referencia": "Cotización de Ejemplo",
  "estado": "EVALUACION",
  "items": [
    {
      "nroItem": 1,
      "descripcion": "Polera Piqué",
      "cantidad": 100,
      "precioUnitario": 5000.0,
      "costoUnitario": 3500.0
    }
  ]
}
```

## Estructura del equipo / Autores
- **Cristopher Osses** - Scrum master / Fullstack Developer / Arquitectura del Sistema
- **Solgrey Medina** -  Product Owner / Fullstack Developer / Arquitectura del sistema

## Tests / Pruebas
Las pruebas unitarias y de integración del backend se encuentran en `Producto/backend/src/test`. Para ejecutarlas:
```bash
cd Producto/backend
mvn test
```

## Licencia
Este proyecto es privado y fue desarrollado para la empresa Antuan Jury S.A. con fines laborales. Reservados todos los derechos.
