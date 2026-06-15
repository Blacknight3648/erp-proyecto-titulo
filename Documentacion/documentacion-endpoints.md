Endpoint: {{baseUrl}}/api/v1/produccion/costeos/disponibles-evn?monto=100

El error que estás viendo **no se debe al parámetro `?monto=100`** que enviaste en la URL (de hecho, este endpoint no recibe parámetros por URL y simplemente los ignora). El error proviene de la base de datos y la forma en que se están mapeando los datos hacia los objetos de dominio.

### 📄 Origen del Error
El error se dispara específicamente en la clase `Money` (ubicada en `backend/com/shared/valueobjects/Money.java` en la línea 20):
```java
public Money(@JsonProperty("amount") BigDecimal amount, @JsonProperty("currency") String currency) {
    if (amount == null) {
        throw new IllegalArgumentException("El monto no puede ser null");
    }
    // ...
}
```
Como se trata de un `IllegalArgumentException` arrojado desde la lógica de dominio (Value Object), tu manejador global de excepciones (Global Exception Handler) lo captura y lo transforma correctamente en un `400 Bad Request`.

### 🔍 ¿A qué se debe exactamente?
1. **Datos en la Base de Datos con valores `NULL`**: 
   En tu archivo `data.sql` estás insertando un registro en la tabla `produccion_costeos` pero sin definir costos, por lo que quedan como `NULL`.

2. **Falla en el Mapeo (`CosteoMapper.java`)**: 
   El mapper intenta ejecutar `new Money(null, "CLP")`, lo cual activa de inmediato la validación de la clase `Money` lanzando la excepción `"El monto no puede ser null"`.

---

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
* `GET {{baseUrl}}/api/v1/comercial/notas-venta`
* `GET {{baseUrl}}/api/v3/maestros/articulos/tipo/PRENDA_LISTA`
* `PUT {{baseUrl}}/api/v1/comercial/evaluaciones-negocio/1`

**Diagnóstico:** Estos endpoints están fallando a nivel de código Java. Puede tratarse de punteros nulos, *LazyInitializationExceptions* (al mapear listas anidadas fuera de transacción) o discrepancias en los mappers.
**Archivos a modificar:** 
- `NotaVentaMapper.java` y `ItemNVMapper.java`: Verificar cómo se mapean los modelos para vincular costos y OPs.
- `ArticuloMapper.java`: Validar el mapeo de los subtipos (tela, prenda, accesorio). Si `PRENDA_LISTA` tiene el detalle nulo en base de datos, el mapper podría estar arrojando NullPointerException.
- `EvaluacionNegocioServiceImpl.java`: Verificar el comportamiento de `CascadeType` al guardar/actualizar los ítems de la EVN en la transacción actual.


