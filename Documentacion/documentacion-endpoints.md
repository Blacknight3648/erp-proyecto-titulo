# Diagnóstico de Pruebas Fallidas en Postman (15/06/2026)

De las pruebas de Postman ejecutadas, 19 presentan problemas. A continuación, se detalla el diagnóstico y los archivos técnicos involucrados:

### 1. Violación de Reglas del Negocio (Errores 422 y 409)
**Endpoints afectados:**
* `POST {{baseUrl}}/api/v1/recepciones-oc/oc/1` (Intentando registrar en estado inválido)
* `POST {{baseUrl}}/api/v1/produccion/ordenes-produccion/recepcionar/1` (OP no PENDIENTE)
* `PUT {{baseUrl}}/api/v1/ordenes-compra/1/items/1/precio` (OC no EMITIDA)
* `DELETE {{baseUrl}}/api/v1/ordenes-compra/1/items/1` y `{{baseUrl}}/api/v1/ordenes-compra/1` (OC procesada)

**Diagnóstico:** El backend está operando correctamente, defendiendo el ciclo de vida del negocio. 
**Archivos a modificar:** Ninguno. Se deben ajustar las pruebas en Postman para asegurar que las entidades estén en el estado correcto (ej. `EMITIDA` o `ENVIADA`) antes de ejecutar estas acciones.

### 2. Errores de Integridad Referencial (Errores 500 al Eliminar)
**Endpoints afectados:**
* `DELETE {{baseUrl}}/api/v3/maestros/categorias-tela/1`
* `DELETE {{baseUrl}}/api/v1/bancos/3`
* `DELETE {{baseUrl}}/api/v1/areas/6`

**Diagnóstico:** Estos registros maestros están siendo referenciados por otras tablas (Foreign Keys), por lo que la base de datos bloquea la eliminación lanzando un `DataIntegrityViolationException`.
**Archivos a modificar:** `GlobalExceptionHandler.java` (o el manejador de excepciones global). Se debe atrapar `DataIntegrityViolationException` y devolver un mensaje estructurado con código HTTP 409 (Conflict) en lugar de un 500.

### 3. Problemas de Unicidad y Datos Ficticios (Errores 500 y 404)
**Endpoints afectados:**
* `PUT {{baseUrl}}/api/v1/vendedores/1` (500 - Falla índice de unicidad)
* `GET {{baseUrl}}/api/v1/recepciones-oc/1` (404)
* `PUT {{baseUrl}}/api/v1/comercial/evaluaciones-negocio/2000/aprobar` (404/422)
* `PUT {{baseUrl}}/api/v3/comercial/descripciones-plantilla/5002` (404)
* `PUT {{baseUrl}}/api/v1/ordenes-compra/2/items/2` (404)

**Diagnóstico:** Las pruebas están usando datos aleatorios generados por Swagger (`"string"`, IDs que no existen). Además, al intentar actualizar un vendedor con un código que ya existe en otro registro, falla la unicidad.
**Archivos a modificar:** Ninguno en el backend. Las pruebas en Postman deben utilizar IDs reales (ej. 1, 2) y datos no repetidos para campos únicos (`codigo`, `rut`).

### 4. Problemas Silenciosos de Actualización (Error 422 o Falso Positivo)
**Endpoints afectados:**
* `PUT {{baseUrl}}/api/v1/tipos-direccion/1`
* `PUT {{baseUrl}}/api/v1/regiones/1`
* `PUT {{baseUrl}}/api/v1/paises/1`

**Diagnóstico:** La validación DTO del backend está rechazando el JSON (falta un campo `@NotNull` o el ID no coincide). La prueba de Postman puede estar asumiendo un 200, pero la BD no actualiza nada.
**Archivos a modificar:** Revisar las restricciones de validación en `RegionDTO`, `PaisDTO` y `TipoDireccionDTO`. Validar que Postman esté mandando el body completo requerido por estos DTOs.

### 5. Errores Internos Reales (Errores 500)
**Endpoints afectados:**
* `GET http://localhost:8050/api/v1/comercial/notas-venta`
* `GET http://localhost:8050/api/v3/maestros/articulos/tipo/PRENDA_LISTA`
* `PUT http://localhost:8050/api/v1/comercial/evaluaciones-negocio/1`

**Diagnóstico:** Estos endpoints están fallando a nivel de código Java. Puede tratarse de punteros nulos, *LazyInitializationExceptions* (al mapear listas anidadas fuera de transacción) o discrepancias en los mappers.
**Archivos a modificar:** 
- `NotaVentaMapper.java` y `ItemNVMapper.java`: Verificar cómo se mapean los modelos para vincular costos y OPs.
- `ArticuloMapper.java`: Validar el mapeo de los subtipos (tela, prenda, accesorio). Si `PRENDA_LISTA` tiene el detalle nulo en base de datos, el mapper podría estar arrojando NullPointerException.
- `EvaluacionNegocioServiceImpl.java`: Verificar el comportamiento de `CascadeType` al guardar/actualizar los ítems de la EVN en la transacción actual.

### 6. Creación de Nota de Venta (Error 500)
**Endpoint afectado:**
* `POST http://localhost:8050/api/v1/comercial/notas-venta`

**Diagnóstico Actualizado:** Inicialmente se pensó que el error era por una violación de llave foránea debido a un desfase entre el ID y el número de documento. Sin embargo, tras validar que `data.sql` fuerza el reinicio de la secuencia a 1000 (`ALTER TABLE evaluaciones_negocio ALTER COLUMN idevn RESTART WITH 1000;`), se confirma que la EVN 1000 **sí existe** correctamente en la base de datos de pruebas. 
Dado que el request logra llegar a procesarse pero retorna un Error 500 genérico, significa que está ocurriendo una excepción no controlada (`Exception` general) a nivel de código Java (probablemente durante el guardado en cascada, la generación de Órdenes de Producción o la respuesta JSON).

**Archivos a modificar:**
Para encontrar la causa raíz exacta sin alterar el código de negocio, se debe:
1. Revisar la consola de la aplicación Spring Boot (`erp_backend`) donde se imprime el log: `[500] Error interno inesperado | ruta=/api/v1/comercial/notas-venta` junto con el *stacktrace* (traza de error) detallado.
2. Una vez obtenida la excepción exacta (ej. `NullPointerException`, `InvalidDataAccessApiUsageException`, etc.), se podrá apuntar al archivo específico (`CrearNVUseCase`, `CrearOrdenProduccionUseCase`, etc.) que requiere el arreglo.